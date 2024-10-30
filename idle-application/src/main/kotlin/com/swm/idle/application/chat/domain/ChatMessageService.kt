package com.swm.idle.application.chat.domain

import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import com.swm.idle.domain.chat.enums.SenderType
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatMessageService {

    fun createByUser(roomId: UUID, userId: UUID, contents: List<ChatMessage.Content>): ChatMessage {
        return ChatMessage(
            roomId = roomId,
            senderId = userId,
            senderType = SenderType.USER,
            contents = contents
        )
    }

    fun publish(it: ChatMessage) {

    }

}
