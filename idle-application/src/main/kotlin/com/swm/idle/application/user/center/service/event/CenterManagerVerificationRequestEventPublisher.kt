package com.swm.idle.application.user.center.service.event

import com.swm.idle.domain.user.center.event.CenterManagerVerificationRequestEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class CenterManagerVerificationRequestEventPublisher(
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun publish(centerManagerVerificationRequestEvent: CenterManagerVerificationRequestEvent) {
        eventPublisher.publishEvent(centerManagerVerificationRequestEvent)
    }

}
