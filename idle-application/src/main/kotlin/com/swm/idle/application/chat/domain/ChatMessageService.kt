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
    /**
     * Creates and persists a new chat message using the provided request data, sender ID, and sequence number.
     *
     * @param request The request containing chat room ID, receiver ID, and message content.
     * @param userId The UUID of the user sending the message.
     * @param sequence The sequence number to assign to the message.
     * @return The saved ChatMessage entity.
     */
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

    /**
     * Retrieves recent chat messages in a chat room after a specified message.
     *
     * @param chatroomId The unique identifier of the chat room.
     * @param messageId The unique identifier of the message after which to retrieve messages.
     * @return A list of recent chat messages following the specified message in the chat room.
     */
    @Transactional
    fun getRecentMessages(chatroomId: UUID, messageId: UUID): List<ChatMessage> {
        return chatMessageRepository.getRecentMessages(chatroomId, messageId)
    }
}
