package com.swm.idle.application.applys.domain

import com.swm.idle.domain.applys.entity.jpa.Applys
import com.swm.idle.domain.applys.repository.CarerApplyJpaRepository
import com.swm.idle.domain.jobposting.vo.ApplyMethodType
import org.springframework.stereotype.Service
import java.util.*

@Service
class CarerApplyService(
    private val carerApplyJpaRepository: CarerApplyJpaRepository,
) {

    fun create(
        jobPostingId: UUID,
        carerId: UUID,
        applyMethodType: ApplyMethodType,
    ) {
        carerApplyJpaRepository.save(
            Applys(
                jobPostingId = jobPostingId,
                carerId = carerId,
                applyMethodType = applyMethodType,
            )
        )
    }

    fun existsByJobPostingIdAndCarerId(
        jobPostingId: UUID,
        carerId: UUID,
    ): Boolean {
        return carerApplyJpaRepository.existsByJobPostingIdAndCarerId(
            jobPostingId = jobPostingId,
            carerId = carerId,
        )
    }

    fun findAllByJobPostingId(jobPostingId: UUID): List<Applys> {
        return carerApplyJpaRepository.findAllByJobPostingId(jobPostingId)
    }

    fun findByJobPostingIdAndCarerId(
        jobPostingId: UUID,
        carerId: UUID,
    ): Applys? {
        return carerApplyJpaRepository.findByJobPostingIdAndCarerId(
            jobPostingId = jobPostingId,
            carerId = carerId,
        )
    }

}
