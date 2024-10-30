package com.swm.idle.infrastructure.fcm.applys.service

import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.swm.idle.domain.applys.event.ApplyEvent
import com.swm.idle.infrastructure.fcm.common.client.FcmClient
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

@Component
class CarerApplyEventService(
    private val fcmClient: FcmClient,
) {

    private val logger = KotlinLogging.logger {}

    fun send(applyEvent: ApplyEvent) {
        val message = createMessage(applyEvent)

        try {
            fcmClient.send(message)
        } catch (e: Exception) {
            logger.warn { "FCM 알림 전송에 실패했습니다 : ${message}" }
        }
    }

    private fun createApplyNotification(applyEvent: ApplyEvent): Notification {
        return Notification.builder()
            .setTitle(applyEvent.notificationInfo.title)
            .setBody(applyEvent.notificationInfo.body)
            .build()
    }

    private fun createMessage(applyEvent: ApplyEvent): Message {
        val applyNotification = createApplyNotification(applyEvent)

        return Message.builder()
            .setToken(applyEvent.deviceToken.deviceToken)
            .setNotification(applyNotification)
            .putData("notificationId", applyEvent.notificationId.toString())
            .putData("notificationType", applyEvent.notificationInfo.notificationType.toString())
            .putData(
                "jobPostingId",
                applyEvent.notificationInfo.notificationDetails!!["jobPostingId"].toString()
            )
            .build();
    }

}
