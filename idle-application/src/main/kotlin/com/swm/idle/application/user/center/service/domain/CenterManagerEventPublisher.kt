package com.swm.idle.application.user.center.service.domain

import com.swm.idle.domain.user.center.event.CenterManagerVerifyEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class CenterManagerEventPublisher(
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun publish(centerManagerVerifyEvent: CenterManagerVerifyEvent) {
        eventPublisher.publishEvent(centerManagerVerifyEvent)
    }

}
