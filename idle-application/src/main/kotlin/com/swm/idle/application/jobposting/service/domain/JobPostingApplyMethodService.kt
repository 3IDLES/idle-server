package com.swm.idle.application.jobposting.service.domain

import com.swm.idle.domain.jobposting.entity.jpa.JobPostingApplyMethod
import com.swm.idle.domain.jobposting.repository.jpa.JobPostingApplyMethodJpaRepository
import com.swm.idle.domain.jobposting.vo.ApplyMethodType
import com.swm.idle.support.common.uuid.UuidCreator
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
                id = UuidCreator.create(),
                jobPostingId = jobPostingId,
                applyMethod = applyMethod
            )
        }.also { jobPostingApplyMethodJpaRepository.saveAll(it) }
    }

}
