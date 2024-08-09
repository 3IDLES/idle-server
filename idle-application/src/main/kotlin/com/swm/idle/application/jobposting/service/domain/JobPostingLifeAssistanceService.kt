package com.swm.idle.application.jobposting.service.domain

import com.swm.idle.domain.jobposting.entity.jpa.JobPosting
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

    fun update(
        jobPosting: JobPosting,
        lifeAssistance: List<LifeAssistanceType>,
    ) {
        val existingLifeAssistances =
            jobPostingLifeAssistanceJpaRepository.findByJobPostingId(jobPosting.id)

        val existingLifeAssistanceSet =
            existingLifeAssistances?.map { it.lifeAssistance }?.toSet() ?: emptySet()
        val newLifeAssistanceSet = lifeAssistance.toSet()

        val toAddLifeAssistances =
            newLifeAssistanceSet.subtract(existingLifeAssistanceSet).map { lifeAssistance ->
                JobPostingLifeAssistance(
                    id = UuidCreator.create(),
                    jobPostingId = jobPosting.id,
                    lifeAssistance = lifeAssistance
                )
            }

        val toDeleteLifeAssistances =
            existingLifeAssistances?.filter { it.lifeAssistance !in newLifeAssistanceSet }?.toSet()

        if (toDeleteLifeAssistances.isNullOrEmpty().not()) {
            jobPostingLifeAssistanceJpaRepository.deleteAll(toDeleteLifeAssistances!!.toList())
        }

        if (toAddLifeAssistances.isEmpty().not()) {
            jobPostingLifeAssistanceJpaRepository.saveAll(toAddLifeAssistances.toList())
        }
    }

}
