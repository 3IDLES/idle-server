package com.swm.idle.application.user.center.service.event

import com.swm.idle.domain.user.center.event.CenterManagerVerificationApproveEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class CenterManagerVerificationApproveEventPublisher(
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun publish(centerManagerVerificationApproveEvent: CenterManagerVerificationApproveEvent) {
        eventPublisher.publishEvent(centerManagerVerificationApproveEvent)
    }

}

