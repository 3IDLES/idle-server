package com.swm.idle.presentation.notification.controller

import com.swm.idle.application.notification.facade.NotificationFacadeService
import com.swm.idle.presentation.notification.api.NotificationApi
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class NotificationController(
    private val notificationFacadeService: NotificationFacadeService,
) : NotificationApi {

    override fun readNotification(notificationId: UUID) {
        notificationFacadeService.readNotification(notificationId)
    }

}
