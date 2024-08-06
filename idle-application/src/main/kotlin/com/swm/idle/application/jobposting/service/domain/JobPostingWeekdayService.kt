package com.swm.idle.application.jobposting.service.domain

import com.swm.idle.domain.jobposting.entity.jpa.JobPostingWeekday
import com.swm.idle.domain.jobposting.repository.jpa.JobPostingWeekdayJpaRepository
import com.swm.idle.domain.jobposting.vo.Weekdays
import com.swm.idle.support.common.uuid.UuidCreator
import org.springframework.stereotype.Service
import java.util.*

@Service
class JobPostingWeekdayService(
    private val jobPostingWeekdayJpaRepository: JobPostingWeekdayJpaRepository,
) {

    fun create(
        jobPostingId: UUID,
        weekdays: List<Weekdays>,
    ) {
        weekdays.map { weekday ->
            JobPostingWeekday(
                id = UuidCreator.create(),
                jobPostingId = jobPostingId,
                weekday = weekday
            )
        }.also { jobPostingWeekdayJpaRepository.saveAll(it) }
    }

}
