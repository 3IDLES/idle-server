package com.swm.idle.application.jobposting.domain

import com.swm.idle.domain.jobposting.entity.jpa.JobPosting
import com.swm.idle.domain.jobposting.entity.jpa.JobPostingLifeAssistance
import com.swm.idle.domain.jobposting.repository.jpa.JobPostingLifeAssistanceJpaRepository
import com.swm.idle.domain.jobposting.vo.LifeAssistanceType
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
            jobPostingLifeAssistanceJpaRepository.findAllByJobPostingId(jobPosting.id)

        val existingLifeAssistanceSet =
            existingLifeAssistances?.map { it.lifeAssistance }?.toSet() ?: emptySet()
        val newLifeAssistanceSet = lifeAssistance.toSet()

        val toAddLifeAssistances =
            newLifeAssistanceSet.subtract(existingLifeAssistanceSet).map { lifeAssistanceType ->
                JobPostingLifeAssistance(
                    jobPostingId = jobPosting.id,
                    lifeAssistance = lifeAssistanceType
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

    fun findByJobPostingId(jobPostingId: UUID): List<JobPostingLifeAssistance>? {
        return jobPostingLifeAssistanceJpaRepository.findAllByJobPostingId(jobPostingId)
    }

    fun deleteAll(jobPostingLifeAssistances: List<JobPostingLifeAssistance>) {
        for (jobPostingLifeAssistance in jobPostingLifeAssistances) {
            jobPostingLifeAssistance.delete()
        }
    }

}
