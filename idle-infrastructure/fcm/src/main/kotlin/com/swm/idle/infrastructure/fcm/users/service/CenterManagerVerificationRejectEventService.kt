package com.swm.idle.infrastructure.fcm.users.service

import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.swm.idle.domain.user.center.event.CenterManagerVerificationRejectEvent
import com.swm.idle.infrastructure.fcm.common.client.FcmClient
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

@Component
class CenterManagerVerificationRejectEventService(
    private val fcmClient: FcmClient,
) {

    private val logger = KotlinLogging.logger {}

    fun send(centerManagerVerificationRejectEvent: CenterManagerVerificationRejectEvent) {
        val message = createMessage(centerManagerVerificationRejectEvent)

        try {
            fcmClient.send(message)
        } catch (e: Exception) {
            logger.warn { "FCM 알림 전송에 실패했습니다 : ${message}, 실패한 Event : CenterManagerVerificationRejectEvent" }
        }
    }


    private fun createJobPostingNotification(centerManagerVerificationRejectEvent: CenterManagerVerificationRejectEvent): Notification {
        return Notification.builder()
            .setTitle(centerManagerVerificationRejectEvent.notificationInfo.title)
            .setBody(centerManagerVerificationRejectEvent.notificationInfo.body)
            .build()
    }

    private fun createMessage(centerManagerVerificationRejectEvent: CenterManagerVerificationRejectEvent): Message {
        val createJobPostingNotification =
            createJobPostingNotification(centerManagerVerificationRejectEvent)

        return Message.builder()
            .setToken(centerManagerVerificationRejectEvent.deviceToken)
            .setNotification(createJobPostingNotification)
            .putData(
                "notificationId",
                centerManagerVerificationRejectEvent.notificationId.toString()
            )
            .putData(
                "notificationType",
                centerManagerVerificationRejectEvent.notificationInfo.notificationType.toString()
            )
            .build();
    }

}
