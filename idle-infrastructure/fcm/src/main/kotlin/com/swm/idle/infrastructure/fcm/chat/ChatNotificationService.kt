package com.swm.idle.infrastructure.fcm.chat

import com.swm.idle.infrastructure.fcm.common.client.FcmClient
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import com.swm.idle.domain.notification.jpa.DeviceToken

@Component
class ChatNotificationService(
    private val fcmClient: FcmClient,
) {
    private val logger = KotlinLogging.logger {}

    fun send(chatMessage: ChatMessage, senderName: String, tokenEntity: DeviceToken?) {
        if (tokenEntity == null) return
        val fcmMessage = createFcmMessage(chatMessage,senderName, tokenEntity.deviceToken)
        clientSend(fcmMessage)
    }

    private fun createFcmMessage(chatMessage: ChatMessage,
                                 senderName: String,
                                 token : String,): Message {
        val notification = Notification.builder()
            .setTitle(senderName)
            .setBody(chatMessage.content)
            .build()

        return Message.builder()
            .setToken(token)
            .setNotification(notification)
            .putData(
                "chatRoomId",
                chatMessage.chatRoomId.toString()
            )
            .build();
    }

    private fun clientSend(fcmMessage: Message) {
        try {
            fcmClient.send(fcmMessage)
        } catch (e: Exception) {
            logger.warn { "FCM 알림 전송에 실패했습니다 : ${fcmMessage}, 실패한 Event : CenterManagerVerificationApproveEvent" }
        }
    }
}