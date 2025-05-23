package com.swm.idle.support.transfer.chat

import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import java.time.LocalDateTime
import java.util.*

class ChatMessageSocketResponse(
    val type: ChatMessageType,
    val id: UUID,
    val chatroomId: UUID,
    val receiverId: UUID,
    val senderId: UUID,
    val content: String,
    val createdAt: LocalDateTime,
) {
    constructor(message: ChatMessage) : this(
        type = ChatMessageType.MESSAGE,
        id = message.id,
        chatroomId = message.chatRoomId,
        receiverId = message.receiverId,
        senderId = message.senderId,
        content = message.content,
        createdAt = message.createdAt ?: LocalDateTime.now(),
    )
}
