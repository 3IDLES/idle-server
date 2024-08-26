package com.swm.idle.application.jobposting.domain

import com.swm.idle.domain.jobposting.entity.jpa.JobPosting
import com.swm.idle.domain.jobposting.entity.jpa.JobPostingApplyMethod
import com.swm.idle.domain.jobposting.repository.jpa.JobPostingApplyMethodJpaRepository
import com.swm.idle.domain.jobposting.vo.ApplyMethodType
import org.springframework.stereotype.Service
import java.util.*

@Service
class JobPostingApplyMethodService(
    private val jobPostingApplyMethodJpaRepository: JobPostingApplyMethodJpaRepository,
) {

    fun create(
        jobPostingId: UUID,
        applyMethods: List<ApplyMethodType>,
    ) {
        applyMethods.map { applyMethod ->
            JobPostingApplyMethod(
                jobPostingId = jobPostingId,
                applyMethod = applyMethod
            )
        }.also { jobPostingApplyMethodJpaRepository.saveAll(it) }
    }

    fun update(
        jobPosting: JobPosting,
        applyMethods: List<ApplyMethodType>,
    ) {
        val existingApplyMethods =
            jobPostingApplyMethodJpaRepository.findAllByJobPostingId(jobPosting.id)

        val existingApplyMethodSet =
            existingApplyMethods?.map { it.applyMethod }?.toSet() ?: emptySet()
        val newApplyMethodSet = applyMethods.toSet()

        val toAddApplyMethods =
            newApplyMethodSet.subtract(existingApplyMethodSet).map { applyMethod ->
                JobPostingApplyMethod(
                    jobPostingId = jobPosting.id,
                    applyMethod = applyMethod
                )
            }

        val toDeleteWeekdays =
            existingApplyMethods?.filter { it.applyMethod !in newApplyMethodSet }?.toSet()

        if (toDeleteWeekdays.isNullOrEmpty().not()) {
            jobPostingApplyMethodJpaRepository.deleteAll(toDeleteWeekdays!!.toList())
        }

        if (toAddApplyMethods.isEmpty().not()) {
            jobPostingApplyMethodJpaRepository.saveAll(toAddApplyMethods.toList())
        }
    }

    fun findByJobPostingId(jobPostingId: UUID): List<JobPostingApplyMethod>? {
        return jobPostingApplyMethodJpaRepository.findAllByJobPostingId(jobPostingId)
    }

    fun deleteAll(jobPostingApplyMethods: List<JobPostingApplyMethod>) {
        for (jobPostingMethod in jobPostingApplyMethods) {
            jobPostingMethod.delete()
        }
    }

}
