package com.swm.idle.support.transfer.chat

import com.swm.idle.domain.chat.vo.ReadMessage
import java.util.*

data class ReadNoti(val chatroomId: UUID, val readByUserId: UUID, val type: ChatMessageType) {
    constructor(message: ReadMessage) : this(
        readByUserId = message.readUserId,
        chatroomId = message.chatRoomId,
        type = ChatMessageType.READ
    )
}