package com.swm.idle.infrastructure.fcm.applys.listener

import com.swm.idle.domain.applys.event.ApplyEvent
import com.swm.idle.infrastructure.fcm.applys.service.CarerApplyEventService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component

class CarerApplyEventListener(
    private val carerApplyEventService: CarerApplyEventService,
) {

    @EventListener
    fun handleCarerApplyEvent(applyEvent: ApplyEvent) {
        carerApplyEventService.send(applyEvent)
    }

}

