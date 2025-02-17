package com.swm.idle.application.chat.domain

import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import com.swm.idle.domain.chat.repository.ChatMessageRepository
import com.swm.idle.support.transfer.chat.ReadChatMessagesReqeust
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChatMessageService (
    private val chatMessageRepository: ChatMessageRepository
){
    @Transactional
    fun save(chatMessage: ChatMessage) {
        chatMessageRepository.save(chatMessage)
    }

    @Transactional
    fun read(request: ReadChatMessagesReqeust, readUserId: UUID) {
        chatMessageRepository.readByChatroomId(request.chatRoomId, readUserId)
    }

    @Transactional
    fun getRecentMessages(chatroomId: UUID, messageId: UUID): List<ChatMessage> {
        return chatMessageRepository.getRecentMessages(chatroomId, messageId)
    }
}
