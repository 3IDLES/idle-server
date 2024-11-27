package com.swm.idle.domain.user.center.event

import com.swm.idle.domain.notification.event.NotificationInfo
import java.util.*

data class CenterManagerVerificationApproveEvent(
    val notificationId: UUID,
    val notificationInfo: NotificationInfo,
    val deviceToken: String,
)