package com.swm.idle.domain.jobposting.repository.querydsl

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.swm.idle.domain.applys.entity.jpa.QApplys.applys
import com.swm.idle.domain.common.dto.JobPostingPreviewDto
import com.swm.idle.domain.common.enums.EntityStatus
import com.swm.idle.domain.jobposting.entity.jpa.QJobPosting.jobPosting
import com.swm.idle.domain.jobposting.entity.jpa.QJobPostingFavorite.jobPostingFavorite
import com.swm.idle.domain.jobposting.entity.jpa.QJobPostingWeekday.jobPostingWeekday
import com.swm.idle.domain.jobposting.enums.JobPostingStatus
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JobPostingSpatialQueryRepository(
    private val jpaQueryFactory: JPAQueryFactory,
) {

    fun findAllWithWeekdaysInRange(
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

        val applysSub = JPAExpressions
            .select(applys.jobPostingId, applys.createdAt)
            .from(applys)
            .where(applys.jobPostingId.`in`(jobPostingIds))

        val favoriteSub = JPAExpressions
            .select(jobPostingFavorite.jobPostingId)
            .from(jobPostingFavorite)
            .where(
                jobPostingFavorite.jobPostingId.`in`(jobPostingIds)
                    .and(jobPostingFavorite.entityStatus.eq(EntityStatus.ACTIVE))
            )

        return jpaQueryFactory
            .selectDistinct(
                Projections.constructor(
                    JobPostingPreviewDto::class.java,
                    jobPosting,
                    jobPostingWeekday,
                    applysSub,
                    favoriteSub.exists()
                )
            )
            .from(jobPosting)
            .leftJoin(jobPostingWeekday).on(jobPosting.id.eq(jobPostingWeekday.jobPostingId))
            .where(jobPosting.id.`in`(jobPostingIds))
            .fetch()
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
