package com.swm.idle.presentation.notification.controller

import com.swm.idle.application.notification.facade.NotificationFacadeService
import com.swm.idle.presentation.notification.api.NotificationApi
import com.swm.idle.support.transfer.jobposting.carer.CursorScrollRequest
import com.swm.idle.support.transfer.notification.NotificationScrollResponse
import com.swm.idle.support.transfer.notification.UnreadNotificationCountResponse
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class NotificationController(
    private val notificationFacadeService: NotificationFacadeService,
) : NotificationApi {

    override fun readNotification(notificationId: UUID) {
        notificationFacadeService.readNotification(notificationId)
    }

    override fun countUnreadNotification(): UnreadNotificationCountResponse {
        return notificationFacadeService.countUnreadNotification()
    }

    override fun getNotifications(request: CursorScrollRequest): NotificationScrollResponse {
        return notificationFacadeService.getNotifications(request)
    }

}
