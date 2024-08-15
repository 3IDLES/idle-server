package com.swm.idle.domain.jobposting.repository.querydsl

import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.group.GroupBy.list
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.swm.idle.domain.common.dto.JobPostingWithWeekdaysDto
import com.swm.idle.domain.jobposting.entity.jpa.QJobPosting.jobPosting
import com.swm.idle.domain.jobposting.entity.jpa.QJobPostingWeekday.jobPostingWeekday
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JobPostingSpatialQueryRepository(
    private val jpaQueryFactory: JPAQueryFactory,
) {

    //    fun findAllWithWeekdaysInRange(
//        location: Point,
//        next: UUID?,
//        limit: Long,
//    ): List<JobPostingWithWeekdaysDto> {
//        return jpaQueryFactory
//            .select(jobPosting, jobPostingWeekday)
//            .from(jobPosting)
//            .leftJoin(jobPostingWeekday).fetchJoin()
//            .on(jobPosting.id.eq(jobPostingWeekday.jobPostingId))
//            .where(
//                isExistInRange(location)
//                    .and(next?.let { jobPosting.id.loe(it) })
//            )
//            .limit(limit)
//            .transform(
//                groupBy(jobPosting.id)
//                    .list(
//                        Projections.constructor(
//                            JobPostingWithWeekdaysDto::class.java,
//                            jobPosting,
//                            list(
//                                jobPostingWeekday
//                            )
//                        )
//                    )
//            )
//
//    }
    fun findAllWithWeekdaysInRange(
        location: Point,
        next: UUID?,
        limit: Long,
    ): List<JobPostingWithWeekdaysDto> {
        // Step 1: 제한된 개수의 JobPosting ID를 가져옴
        val jobPostingIds = jpaQueryFactory
            .select(jobPosting.id)
            .from(jobPosting)
            .where(
                isExistInRange(location)
                    .and(next?.let { jobPosting.id.goe(it) })
            )
            .limit(limit)
            .fetch()

        if (jobPostingIds.isEmpty()) {
            return emptyList()
        }

        // Step 2: 가져온 JobPosting ID에 해당하는 JobPosting과 그에 연결된 Weekdays를 가져옴
        return jpaQueryFactory
            .select(jobPosting, jobPostingWeekday)
            .from(jobPosting)
            .leftJoin(jobPostingWeekday).fetchJoin()
            .on(jobPosting.id.eq(jobPostingWeekday.jobPostingId))
            .where(jobPosting.id.`in`(jobPostingIds))
            .transform(
                groupBy(jobPosting.id)
                    .list(
                        Projections.constructor(
                            JobPostingWithWeekdaysDto::class.java,
                            jobPosting,
                            list(jobPostingWeekday)
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
