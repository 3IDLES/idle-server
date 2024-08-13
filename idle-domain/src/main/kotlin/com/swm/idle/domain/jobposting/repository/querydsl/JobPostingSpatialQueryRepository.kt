package com.swm.idle.domain.jobposting.repository.querydsl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.ComparablePath
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.swm.idle.domain.jobposting.entity.jpa.JobPosting
import com.swm.idle.domain.jobposting.entity.jpa.QJobPosting.jobPosting
import com.swm.idle.domain.jobposting.entity.jpa.QJobPostingWeekday.jobPostingWeekday
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JobPostingSpatialQueryRepository(
    private val jpaQueryFactory: JPAQueryFactory,
) {

    fun findByInRange(
        jobPostingId: UUID,
        location: Point,
        pageSize: Long,
    ): List<JobPosting> {

        return jpaQueryFactory
            .selectFrom(jobPosting)
            .innerJoin(jobPostingWeekday).on(jobPosting.id.eq(jobPostingWeekday.jobPostingId))
            .fetchJoin()
            .where(
                isExistInRange(location)?.and(lowerThanJobPostingId(jobPosting.id))
            )
            .orderBy(jobPosting.id.desc())
            .limit(pageSize)
            .fetch();
    }

    private fun isExistInRange(location: Point): BooleanExpression? {
        if (location == null) {
            return null;
        }

        return Expressions.booleanTemplate(
            "ST_Contains(ST_BUFFER({0}, 3000, {1})",
            location,
            jobPosting.location
        )
    }

    private fun lowerThanJobPostingId(jobPostingId: ComparablePath<UUID>): BooleanExpression? {
        if (jobPostingId == null) {
            return null;
        }

        return jobPosting.id.lt(jobPostingId)
    }
}
