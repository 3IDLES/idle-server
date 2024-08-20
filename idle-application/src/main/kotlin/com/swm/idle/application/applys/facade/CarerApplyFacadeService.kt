package com.swm.idle.application.applys.facade

import com.swm.idle.application.applys.domain.CarerApplyService
import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.domain.applys.exception.ApplyException
import com.swm.idle.domain.jobposting.vo.ApplyMethodType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class CarerApplyFacadeService(
    private val carerApplyService: CarerApplyService,
) {

    @Transactional
    fun createApply(
        jobPostingId: UUID,
        applyMethodType: ApplyMethodType,
    ) {
        val carerId = getUserAuthentication().userId

        if (carerApplyService.existsByJobPostingIdAndCarerIdAndApplyMethodType(
                jobPostingId = jobPostingId,
                carerId = carerId,
                applyMethodType = applyMethodType,
            )
        ) {
            throw ApplyException.AlreadyApplied()
        }

        carerApplyService.create(
            jobPostingId = jobPostingId,
            carerId = carerId,
            applyMethodType = applyMethodType,
        )
    }

}
