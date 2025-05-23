package com.swm.idle.application.chat.domain

import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import com.swm.idle.domain.chat.repository.ChatMessageRepository
import com.swm.idle.support.transfer.chat.SendChatMessageRequest
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatMessageService (
    private val chatMessageRepository: ChatMessageRepository
){
    @Transactional
    fun save(request: SendChatMessageRequest, userId: UUID, sequence: Long): ChatMessage {
        val message = ChatMessage(
            chatRoomId = UUID.fromString(request.chatroomId),
            content = request.content,
            senderId = userId,
            receiverId = UUID.fromString(request.receiverId),
            sequence = sequence
        )
        return chatMessageRepository.save(message)
    }

    @Transactional
    fun getRecentMessages(chatroomId: UUID, messageId: UUID): List<ChatMessage> {
        return chatMessageRepository.getRecentMessages(chatroomId, messageId)
    }
}
