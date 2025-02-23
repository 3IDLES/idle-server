package com.swm.idle.support.transfer.chat

import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import java.util.UUID
import java.time.LocalDateTime

data class ChatMessageResponse(
    val id: UUID,
    val chatRoomId: UUID,
    val senderId: UUID,
    val receiverId: UUID,
    val content: String,
    val createdAt: LocalDateTime?
) {
    constructor(message: ChatMessage) : this(
        id = message.id,
        chatRoomId = message.chatRoomId,
        senderId = message.senderId,
        receiverId = message.receiverId,
        content = message.content,
        createdAt = message.createdAt
    )
}