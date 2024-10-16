package com.swm.idle.application.notification.facade

import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.notification.domain.NotificationService
import com.swm.idle.support.security.exception.SecurityException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class NotificationFacadeService(
    private val notificationService: NotificationService,
) {

    fun readNotification(notificationId: UUID) {
        val notification = notificationService.getById(notificationId)

        if (notification.receiverId != getUserAuthentication().userId) {
            throw SecurityException.UnAuthorizedRequest()
        }

        notificationService.getById(notificationId).also {
            notificationService.read(it)
        }
    }

}
