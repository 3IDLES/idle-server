package com.swm.idle.domain.chat.vo

import java.util.*

data class ReadMessage(
    val chatRoomId: UUID,
    val receiverId: UUID,
    val readUserId: UUID,)
