package com.swm.idle.domain.jobposting.repository.querydsl

import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.swm.idle.domain.common.dto.CrawlingJobPostingPreviewDto
import com.swm.idle.domain.common.enums.EntityStatus
import com.swm.idle.domain.jobposting.entity.jpa.QCrawledJobPosting.crawledJobPosting
import com.swm.idle.domain.jobposting.entity.jpa.QJobPostingFavorite.jobPostingFavorite
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class CrawlingJobPostingSpatialQueryRepository(
    private val jpaQueryFactory: JPAQueryFactory,
) {

    fun findAllInRange(
        carerId: UUID,
        location: Point,
        next: UUID?,
        limit: Long,
    ): List<CrawlingJobPostingPreviewDto> {
        val crawledJobPostingIds = jpaQueryFactory
            .select(crawledJobPosting.id)
            .from(crawledJobPosting)
            .where(
                isExistInRange(location)
                    .and(next?.let { crawledJobPosting.id.goe(it) })
                    .and(crawledJobPosting.entityStatus.eq(EntityStatus.ACTIVE))
            )
            .limit(limit)
            .fetch()

        if (crawledJobPostingIds.isEmpty()) {
            return emptyList()
        }

        return jpaQueryFactory
            .select(crawledJobPosting, jobPostingFavorite)
            .from(crawledJobPosting)
            .leftJoin(jobPostingFavorite)
            .on(
                crawledJobPosting.id.eq(jobPostingFavorite.jobPostingId)
                    .and(jobPostingFavorite.carerId.eq(carerId))
            )
            .where(crawledJobPosting.id.`in`(crawledJobPostingIds))
            .transform(
                groupBy(crawledJobPosting.id)
                    .list(
                        Projections.constructor(
                            CrawlingJobPostingPreviewDto::class.java,
                            crawledJobPosting,
                            jobPostingFavorite.id.isNotNull
                                .and(jobPostingFavorite.entityStatus.eq(EntityStatus.ACTIVE))
                        )
                    )
            )
    }

    private fun isExistInRange(
        location: Point,
    ): BooleanExpression {
        return Expressions.booleanTemplate(
            "ST_Contains(ST_BUFFER({0}, 5000), {1})",
            location,
            crawledJobPosting.location,
        )
    }

}
