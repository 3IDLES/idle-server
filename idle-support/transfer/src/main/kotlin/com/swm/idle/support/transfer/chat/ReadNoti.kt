package com.swm.idle.support.transfer.chat

import com.swm.idle.domain.chat.vo.ReadMessage
import java.util.*

data class ReadNoti(val chatRoomId: UUID, val readByUserId: UUID) {
    constructor(message: ReadMessage) : this(
        readByUserId = message.readUserId,
        chatRoomId = message.chatRoomId
    )
}