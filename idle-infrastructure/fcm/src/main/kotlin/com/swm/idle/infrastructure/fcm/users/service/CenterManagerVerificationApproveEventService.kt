package com.swm.idle.infrastructure.fcm.users.service

import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.swm.idle.domain.user.center.event.CenterManagerVerificationApproveEvent
import com.swm.idle.infrastructure.fcm.common.client.FcmClient
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

@Component
class CenterManagerVerificationApproveEventService(
    private val fcmClient: FcmClient,
) {

    private val logger = KotlinLogging.logger {}

    fun send(centerManagerVerificationApproveEvent: CenterManagerVerificationApproveEvent) {
        val message = createMessage(centerManagerVerificationApproveEvent)

        try {
            fcmClient.send(message)
        } catch (e: Exception) {
            logger.warn { "FCM 알림 전송에 실패했습니다 : ${message}, 실패한 Event : CenterManagerVerificationApproveEvent" }
        }
    }


    private fun createJobPostingNotification(centerManagerVerificationApproveEvent: CenterManagerVerificationApproveEvent): Notification {
        return Notification.builder()
            .setTitle(centerManagerVerificationApproveEvent.notificationInfo.title)
            .setBody(centerManagerVerificationApproveEvent.notificationInfo.body)
            .build()
    }

    private fun createMessage(centerManagerVerificationApproveEvent: CenterManagerVerificationApproveEvent): Message {
        val createJobPostingNotification =
            createJobPostingNotification(centerManagerVerificationApproveEvent)

        return Message.builder()
            .setToken(centerManagerVerificationApproveEvent.deviceToken)
            .setNotification(createJobPostingNotification)
            .putData(
                "notificationId",
                centerManagerVerificationApproveEvent.notificationId.toString()
            )
            .putData(
                "notificationType",
                centerManagerVerificationApproveEvent.notificationInfo.notificationType.toString()
            )
            .build();
    }


}
