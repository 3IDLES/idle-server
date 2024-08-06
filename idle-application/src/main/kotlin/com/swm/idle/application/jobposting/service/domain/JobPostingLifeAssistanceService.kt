package com.swm.idle.application.jobposting.service.domain

import com.swm.idle.domain.jobposting.entity.jpa.JobPostingLifeAssistance
import com.swm.idle.domain.jobposting.repository.jpa.JobPostingLifeAssistanceJpaRepository
import com.swm.idle.domain.jobposting.vo.LifeAssistanceType
import com.swm.idle.support.common.uuid.UuidCreator
import org.springframework.stereotype.Service
import java.util.*

@Service
class JobPostingLifeAssistanceService(
    private val jobPostingLifeAssistanceJpaRepository: JobPostingLifeAssistanceJpaRepository,
) {

    fun create(
        jobPostingId: UUID,
        lifeAssistance: List<LifeAssistanceType>,
    ) {
        lifeAssistance.map { lifeAssistanceType ->
            JobPostingLifeAssistance(
                id = UuidCreator.create(),
                jobPostingId = jobPostingId,
                lifeAssistance = lifeAssistanceType,
            )
        }.also {
            jobPostingLifeAssistanceJpaRepository.saveAll(it)
        }
    }

}
