package com.swm.idle.support.transfer.chat

import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import java.time.LocalDateTime
import java.util.*

data class ChatMessageInfo(
    val type: ChatMessageType,
    val id: UUID,
    val chatroomId: UUID,
    val senderId: UUID,
    val receiverId: UUID,
    val content: String,
    val createdAt: LocalDateTime,
    val sequence: Long,
) {
    constructor(message: ChatMessage) : this(
        type = ChatMessageType.MESSAGE,
        id = message.id,
        chatroomId = message.chatRoomId,
        senderId = message.senderId,
        receiverId = message.receiverId,
        content = message.content,
        createdAt = message.createdAt ?: LocalDateTime.now(),
        sequence = message.sequence,
    )
}