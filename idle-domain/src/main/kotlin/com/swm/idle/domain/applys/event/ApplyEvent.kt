package com.swm.idle.domain.applys.event

import com.swm.idle.domain.notification.jpa.DeviceToken
import com.swm.idle.domain.notification.jpa.NotificationInfo
import java.util.*

data class ApplyEvent(
    val deviceToken: DeviceToken,
    val notificationId: UUID,
    val notificationInfo: NotificationInfo,
) {

    companion object {

        fun createApplyEvent(
            deviceToken: DeviceToken,
            notificationId: UUID,
            notificationInfo: NotificationInfo,
        ): ApplyEvent {
            return ApplyEvent(deviceToken, notificationId, notificationInfo)
        }

    }

}
