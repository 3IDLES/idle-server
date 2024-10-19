package com.swm.idle.domain.common.dto

import com.swm.idle.domain.notification.enums.NotificationType
import java.time.LocalDateTime
import java.util.*

data class NotificationQueryDto(
    val id: UUID,
    val isRead: Boolean,
    val title: String,
    val body: String,
    val notificationType: NotificationType,
    val createdAt: LocalDateTime,
    val imageUrl: String?,
    var notificationDetails: String,
)
