package com.swm.idle.application.applyment.domain

import com.swm.idle.domain.apply.entity.jpa.Apply
import com.swm.idle.domain.apply.repository.CarerApplyJpaRepository
import com.swm.idle.domain.jobposting.vo.ApplyMethodType
import org.springframework.stereotype.Service
import java.util.*

@Service
class CarerApplyService(
    private val carerApplyJpaRepository: CarerApplyJpaRepository,
) {

    fun existsByJobPostingIdAndCarerIdAndApplyMethodType(
        jobPostingId: UUID,
        carerId: UUID,
        applyMethodType: ApplyMethodType,
    ): Boolean {
        return carerApplyJpaRepository.existsByJobPostingIdAndCarerIdAndApplyMethodType(
            jobPostingId = jobPostingId,
            carerId = carerId,
            applyMethodType = applyMethodType,
        )
    }

    fun create(
        jobPostingId: UUID,
        carerId: UUID,
        applyMethodType: ApplyMethodType,
    ) {
        carerApplyJpaRepository.save(
            Apply(
                jobPostingId = jobPostingId,
                carerId = carerId,
                applyMethodType = applyMethodType,
            )
        )
    }

}
