package com.swm.idle.application.user.center.service.event

import com.swm.idle.domain.user.center.event.CenterManagerVerificationRejectEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class CenterManagerVerificationRejectEventPublisher(
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun publish(centerManagerVerificationRejectEvent: CenterManagerVerificationRejectEvent) {
        eventPublisher.publishEvent(centerManagerVerificationRejectEvent)
    }

}

