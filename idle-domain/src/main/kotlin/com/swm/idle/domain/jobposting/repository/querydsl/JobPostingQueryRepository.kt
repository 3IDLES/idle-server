package com.swm.idle.domain.jobposting.repository.querydsl

import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.swm.idle.domain.applys.entity.jpa.QApplys.applys
import com.swm.idle.domain.common.dto.JobPostingWithWeekdaysAndApplyDto
import com.swm.idle.domain.jobposting.entity.jpa.QJobPosting.jobPosting
import com.swm.idle.domain.jobposting.entity.jpa.QJobPostingWeekday.jobPostingWeekday
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JobPostingQueryRepository(
    private val jpaQueryFactory: JPAQueryFactory,
) {

    fun findAllByCarerApplyHistory(
        next: UUID?,
        limit: Long,
        carerId: UUID,
    ): List<JobPostingWithWeekdaysAndApplyDto> {
        val jobPostingIds = jpaQueryFactory
            .selectDistinct(jobPosting.id)
            .from(jobPosting)
            .leftJoin(applys).on(jobPosting.id.eq(applys.jobPostingId))
            .where(
                applys.carerId.eq(carerId)
                    .and(next?.let { jobPosting.id.goe(it) })
            )
            .limit(limit)
            .fetch()

        if (jobPostingIds.isEmpty()) {
            return emptyList()
        }

        return jpaQueryFactory
            .select(jobPosting, jobPostingWeekday, applys)
            .from(jobPosting)
            .leftJoin(jobPostingWeekday).fetchJoin()
            .on(jobPosting.id.eq(jobPostingWeekday.jobPostingId))
            .leftJoin(applys).fetchJoin()
            .on(jobPosting.id.eq(applys.jobPostingId))
            .where(jobPosting.id.`in`(jobPostingIds))
            .transform(
                groupBy(jobPosting.id)
                    .list(
                        Projections.constructor(
                            JobPostingWithWeekdaysAndApplyDto::class.java,
                            jobPosting,
                            list(jobPostingWeekday),
                            applys.createdAt,
                        )
                    )
            )
    }

}
