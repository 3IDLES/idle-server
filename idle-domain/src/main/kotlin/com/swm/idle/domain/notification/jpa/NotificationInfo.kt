package com.swm.idle.domain.notification.jpa

import com.swm.idle.domain.notification.enums.NotificationType
import java.util.*

interface NotificationInfo {

    val title: String
    val body: String
    val receiverId: UUID
    val notificationType: NotificationType
    val imageUrl: String?
    val notificationDetails: Map<String, Any>?
}
