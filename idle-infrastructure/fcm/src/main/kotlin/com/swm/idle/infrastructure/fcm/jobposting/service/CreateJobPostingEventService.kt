package com.swm.idle.infrastructure.fcm.jobposting.service

import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.swm.idle.domain.jobposting.event.CreateJobPostingEvent
import com.swm.idle.infrastructure.fcm.common.client.FcmClient
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

@Component
class CreateJobPostingEventService(
    private val fcmClient: FcmClient,
) {

    private val logger = KotlinLogging.logger {}

    fun send(createJobPostingEvent: CreateJobPostingEvent) {
        val message = createMessage(createJobPostingEvent)

        try {
            fcmClient.send(message)
        } catch (e: Exception) {
            logger.warn { "FCM 알림 전송에 실패했습니다 : ${message}, 실패한 Event : CreateJobPostingEvent" }
        }
    }

    private fun createJobPostingNotification(createJobPostingEvent: CreateJobPostingEvent): Notification {
        return Notification.builder()
            .setTitle(createJobPostingEvent.notificationInfo.title)
            .setBody(createJobPostingEvent.notificationInfo.body)
            .build()
    }

    private fun createMessage(createJobPostingEvent: CreateJobPostingEvent): Message {
        val createJobPostingNotification = createJobPostingNotification(createJobPostingEvent)

        return Message.builder()
            .setToken(createJobPostingEvent.deviceToken.deviceToken)
            .setNotification(createJobPostingNotification)
            .putData("notificationId", createJobPostingEvent.notificationId.toString())
            .putData(
                "notificationType",
                createJobPostingEvent.notificationInfo.notificationType.toString()
            )
            .putData(
                "jobPostingId",
                createJobPostingEvent.notificationInfo.notificationDetails!!["jobPostingId"].toString()
            )
            .build();
    }

}
