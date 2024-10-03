package com.swm.idle.application.applys.domain

import com.swm.idle.domain.applys.event.ApplyEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class CarerApplyEventPublisher(
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun publish(applyEvent: ApplyEvent) {
        eventPublisher.publishEvent(applyEvent)
    }

}
