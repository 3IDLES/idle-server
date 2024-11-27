package com.swm.idle.infrastructure.fcm.users.listener

import com.swm.idle.domain.user.center.event.CenterManagerVerificationApproveEvent
import com.swm.idle.infrastructure.fcm.users.service.CenterManagerVerificationApproveEventService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class CenterManagerVerificationApproveEventListener(
    private val centerManagerVerificationApproveEventService: CenterManagerVerificationApproveEventService,
) {

    @EventListener
    fun handleCenterManagerVerifyApproveEvent(centerManagerVerificationApproveEvent: CenterManagerVerificationApproveEvent) {
        centerManagerVerificationApproveEventService.send(centerManagerVerificationApproveEvent)
    }
}
