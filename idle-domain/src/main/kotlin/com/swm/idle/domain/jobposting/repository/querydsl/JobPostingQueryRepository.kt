package com.swm.idle.domain.jobposting.repository.querydsl

import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.swm.idle.domain.applys.entity.jpa.QApplys.applys
import com.swm.idle.domain.common.dto.JobPostingPreviewDto
import com.swm.idle.domain.common.enums.EntityStatus
import com.swm.idle.domain.jobposting.entity.jpa.QJobPosting.jobPosting
import com.swm.idle.domain.jobposting.entity.jpa.QJobPostingFavorite.jobPostingFavorite
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
    ): List<JobPostingPreviewDto> {
        val jobPostingIds = jpaQueryFactory
            .select(jobPosting.id)
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
            .select(jobPosting, jobPostingWeekday, applys, jobPostingFavorite)
            .from(jobPosting)
            .leftJoin(jobPostingWeekday).fetchJoin()
            .on(jobPosting.id.eq(jobPostingWeekday.jobPostingId))
            .leftJoin(applys).fetchJoin()
            .on(jobPosting.id.eq(applys.jobPostingId))
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
                            applys.createdAt,
                            jobPostingFavorite.id.isNotNull
                                .and(jobPostingFavorite.entityStatus.eq(EntityStatus.ACTIVE))
                        )
                    )
            )
    }

    fun findAllInFavorites(carerId: UUID): List<JobPostingPreviewDto>? {
        return jpaQueryFactory
            .select(jobPosting, jobPostingWeekday, applys)
            .from(jobPosting)
            .leftJoin(jobPostingWeekday).fetchJoin()
            .on(jobPosting.id.eq(jobPostingWeekday.jobPostingId))
            .leftJoin(applys)
            .on(jobPosting.id.eq(applys.jobPostingId))
            .innerJoin(jobPostingFavorite).fetchJoin()
            .on(
                jobPosting.id.eq(jobPostingFavorite.jobPostingId)
                    .and(jobPostingFavorite.carerId.eq(carerId))
                    .and(jobPostingFavorite.entityStatus.eq(EntityStatus.ACTIVE))
            )
            .transform(
                groupBy(jobPosting.id)
                    .list(
                        Projections.constructor(
                            JobPostingPreviewDto::class.java,
                            jobPosting,
                            list(jobPostingWeekday),
                            applys.createdAt ?: null,
                        )
                    )
            )
    }

}
