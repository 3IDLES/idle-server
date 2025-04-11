package com.swm.idle.support.transfer.chat

import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import java.util.UUID
import java.time.LocalDateTime

data class ChatMessageResponse(
    val type: ChatMessageType,
    val id: UUID,
    val chatroomId: UUID,
    val senderId: UUID,
    val receiverId: UUID,
    val content: String,
    val createdAt: LocalDateTime,
    val isRead: Boolean
) {
    constructor(message: ChatMessage) : this(
        type = ChatMessageType.MESSAGE,
        id = message.id,
        chatroomId = message.chatRoomId,
        senderId = message.senderId,
        receiverId = message.receiverId,
        content = message.content,
        isRead = message.isRead,
        createdAt = message.createdAt ?: LocalDateTime.now(),
    )
}