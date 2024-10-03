package com.swm.idle.application.applys.facade

import com.swm.idle.application.applys.domain.CarerApplyEventPublisher
import com.swm.idle.application.applys.domain.CarerApplyService
import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.jobposting.domain.JobPostingService
import com.swm.idle.application.notification.domain.DeviceTokenService
import com.swm.idle.application.user.carer.domain.CarerService
import com.swm.idle.domain.applys.event.ApplyEvent
import com.swm.idle.domain.applys.exception.ApplyException
import com.swm.idle.domain.jobposting.enums.ApplyMethodType
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class CarerApplyFacadeService(
    private val carerApplyService: CarerApplyService,
    private val carerApplyEventPublisher: CarerApplyEventPublisher,
    private val deviceTokenService: DeviceTokenService,
    private val jobPostingService: JobPostingService,
    private val carerService: CarerService,
) {

    private val logger = KotlinLogging.logger { }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun createApply(
        jobPostingId: UUID,
        applyMethodType: ApplyMethodType,
    ) {
        val carer = getUserAuthentication().userId.let {
            carerService.getById(it)
        }
        val deviceToken = deviceTokenService.findByUserId(carer.id)
        val jobPosting = jobPostingService.getById(jobPostingId)

        if (carerApplyService.existsByJobPostingIdAndCarerId(
                jobPostingId = jobPostingId,
                carerId = carer.id,
            )
        ) {
            throw ApplyException.AlreadyApplied()
        }

        carerApplyService.create(jobPostingId, carer.id, applyMethodType)

        deviceToken?.let {
            carerApplyEventPublisher.publish(
                ApplyEvent.createApplyEvent(
                    deviceToken = deviceToken,
                    jobPosting = jobPosting,
                    carer = carer,
                )
            )
        } ?: run {
            logger.warn { "${carer.id} 요양 보호사의 device Token이 존재하지 않아 알림이 발송되지 않았습니다." }
        }

    }

}
