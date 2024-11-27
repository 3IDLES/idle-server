package com.swm.idle.application.chat.facade

import com.swm.idle.application.chat.domain.ChatMessageService
import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import com.swm.idle.domain.chat.event.ChatMessageRedisPublisher
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatMessageFacadeService(
    private val chatMessageRedisPublisher: ChatMessageRedisPublisher,
    private val chatMessageService: ChatMessageService,
) {

    fun sendTextMessage(
        roomId: UUID,
        senderId: UUID,
        contents: List<ChatMessage.Content>,
    ) {
        chatMessageService.createByUser(
            roomId = roomId,
            userId = senderId,
            contents = contents,
        ).also {
            chatMessageRedisPublisher.publish(it)
        }
    }

    fun saveMessage(chatMessage: ChatMessage) {
        // TODO : 메세지 저장 로직 구현
    }

}
