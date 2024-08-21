package com.swm.idle.application.applys.domain

import com.swm.idle.domain.applys.repository.CarerApplyJpaRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ApplicantService(
    private val carerApplyJpaRepository: CarerApplyJpaRepository,
) {

    fun countByJobPostingId(jobPostingId: UUID): Int {
        return carerApplyJpaRepository.countByJobPostingId(jobPostingId)
    }

}
