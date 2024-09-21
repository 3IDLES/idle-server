package com.swm.idle.domain.jobposting.repository.querydsl

import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.swm.idle.domain.applys.entity.jpa.QApplys.applys
import com.swm.idle.domain.common.dto.JobPostingPreviewDto
import com.swm.idle.domain.common.enums.EntityStatus
import com.swm.idle.domain.jobposting.entity.jpa.QJobPosting.jobPosting
import com.swm.idle.domain.jobposting.entity.jpa.QJobPostingFavorite.jobPostingFavorite
import com.swm.idle.domain.jobposting.entity.jpa.QJobPostingWeekday.jobPostingWeekday
import com.swm.idle.domain.jobposting.enums.JobPostingStatus
import com.swm.idle.domain.user.carer.entity.jpa.Carer
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JobPostingSpatialQueryRepository(
    private val jpaQueryFactory: JPAQueryFactory,
) {

    fun findAllWithWeekdaysInRange(
        carer: Carer,
        location: Point,
        next: UUID?,
        limit: Long,
    ): List<JobPostingPreviewDto> {
        val jobPostingIds = jpaQueryFactory
            .select(jobPosting.id)
            .from(jobPosting)
            .where(
                isExistInRange(location)
                    .and(next?.let { jobPosting.id.goe(it) })
                    .and(jobPosting.jobPostingStatus.eq(JobPostingStatus.IN_PROGRESS))
                    .and(jobPosting.entityStatus.eq(EntityStatus.ACTIVE))
            )
            .limit(limit)
            .fetch()

        if (jobPostingIds.isEmpty()) {
            return emptyList()
        }

        return jpaQueryFactory
            .selectDistinct(
                jobPosting,
                jobPostingWeekday,
                applys,
                jobPostingFavorite
            )
            .from(jobPosting)
            .leftJoin(jobPostingWeekday).fetchJoin()
            .on(jobPosting.id.eq(jobPostingWeekday.jobPostingId))
            .leftJoin(applys)
            .on(
                jobPosting.id.eq(applys.jobPostingId)
                    .and(applys.carerId.eq(carer.id))
            )
            .leftJoin(jobPostingFavorite).fetchJoin()
            .on(jobPosting.id.eq(jobPostingFavorite.jobPostingId))
            .where(jobPosting.id.`in`(jobPostingIds))
            .transform(
                groupBy(jobPosting.id)
                    .list(
                        Projections.constructor(
                            JobPostingPreviewDto::class.java,
                            jobPosting,
                            list(jobPostingWeekday),
                            applys.createdAt ?: null,
                            Expressions.booleanTemplate(
                                "case when {0} is not null and {1} = {2} then true else false end",
                                jobPostingFavorite.id,
                                jobPostingFavorite.entityStatus,
                                EntityStatus.ACTIVE
                            )
                        )
                    )
            )
    }

    private fun isExistInRange(
        location: Point,
    ): BooleanExpression {
        return Expressions.booleanTemplate(
            "ST_Contains(ST_BUFFER({0}, 3000), {1})",
            location,
            jobPosting.location,
        )
    }

}
