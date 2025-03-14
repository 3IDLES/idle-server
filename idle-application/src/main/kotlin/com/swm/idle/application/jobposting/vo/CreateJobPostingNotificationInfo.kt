package com.swm.idle.application.jobposting.vo

import com.swm.idle.application.applys.vo.CarerApplyNotificationInfo
import com.swm.idle.domain.notification.enums.NotificationType
import com.swm.idle.domain.notification.event.NotificationInfo
import java.util.*

class CreateJobPostingNotificationInfo(
    override val title: String,
    override val body: String,
    override val receiverId: UUID,
    override val notificationType: NotificationType,
    override val imageUrl: String?,
    override val notificationDetails: Map<String, Any>?,
) : NotificationInfo {

    companion object {

        fun create(
            title: String,
            body: String,
            receiverId: UUID,
            notificationType: NotificationType,
            imageUrl: String?,
            jobPostingId: UUID,
        ): CarerApplyNotificationInfo {
            val notificationDetails = mapOf(
                "jobPostingId" to jobPostingId,
            )
            return CarerApplyNotificationInfo(
                title,
                body,
                receiverId,
                notificationType,
                imageUrl,
                notificationDetails,
            )
        }

    }

}
