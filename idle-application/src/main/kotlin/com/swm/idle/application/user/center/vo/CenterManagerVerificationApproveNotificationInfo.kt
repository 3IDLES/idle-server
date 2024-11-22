package com.swm.idle.application.user.center.vo

import com.swm.idle.domain.notification.enums.NotificationType
import com.swm.idle.domain.notification.event.NotificationInfo
import java.util.*

data class CenterManagerVerificationApproveNotificationInfo(
    override val title: String,
    override val body: String,
    override val receiverId: UUID,
    override val notificationType: NotificationType,
    override val imageUrl: String?,
    override val notificationDetails: Map<String, Any>?,
) : NotificationInfo {

    companion object {

        fun of(
            title: String,
            body: String,
            receiverId: UUID,
            notificationType: NotificationType,
        ): CenterManagerVerificationApproveNotificationInfo {
            return CenterManagerVerificationApproveNotificationInfo(
                title,
                body,
                receiverId,
                notificationType,
                null,
                null
            )
        }

    }
}
