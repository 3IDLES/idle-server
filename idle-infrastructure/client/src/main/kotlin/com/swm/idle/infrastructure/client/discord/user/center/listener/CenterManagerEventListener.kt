package com.swm.idle.infrastructure.client.discord.user.center.listener

import com.swm.idle.domain.user.center.event.CenterManagerVerifyEvent
import com.swm.idle.infrastructure.client.discord.user.center.service.CenterManagerVerifyEventService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class CenterManagerEventListener(
    private val centerMangerVerifyEventService: CenterManagerVerifyEventService,
) {

    @EventListener
    fun handleCenterManagerVerifyEvent(centerManagerVerifyEvent: CenterManagerVerifyEvent) {
        centerMangerVerifyEventService.sendVerifyMessage(centerManagerVerifyEvent)
    }

}
