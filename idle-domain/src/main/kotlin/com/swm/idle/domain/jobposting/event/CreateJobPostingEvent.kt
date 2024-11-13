package com.swm.idle.domain.jobposting.event

import com.swm.idle.domain.notification.event.NotificationInfo
import com.swm.idle.domain.notification.jpa.DeviceToken
import java.util.*

data class CreateJobPostingEvent(
    val deviceToken: DeviceToken,
    val notificationId: UUID,
    val notificationInfo: NotificationInfo,
) {

    companion object {

        fun of(
            deviceToken: DeviceToken,
            notificationId: UUID,
            notificationInfo: NotificationInfo,
        ): CreateJobPostingEvent {
            return CreateJobPostingEvent(
                deviceToken = deviceToken,
                notificationId = notificationId,
                notificationInfo = notificationInfo
            )
        }
    }

}
