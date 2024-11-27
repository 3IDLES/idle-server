package com.swm.idle.infrastructure.fcm.users.listener

import com.swm.idle.domain.user.center.event.CenterManagerVerificationRejectEvent
import com.swm.idle.infrastructure.fcm.users.service.CenterManagerVerificationRejectEventService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class CenterManagerVerificationRejectEventListener(
    private val centerManagerVerificationRejectEventService: CenterManagerVerificationRejectEventService,
) {

    @EventListener
    fun handleCenterManagerVerifyRejectEvent(centerManagerVerificationRejectEvent: CenterManagerVerificationRejectEvent) {
        centerManagerVerificationRejectEventService.send(centerManagerVerificationRejectEvent)
    }

}
