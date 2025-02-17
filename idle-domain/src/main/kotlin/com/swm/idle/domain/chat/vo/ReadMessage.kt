package com.swm.idle.domain.chat.vo

import java.util.*

data class ReadMessage(val chatroomId: UUID,
                       val readUserId: UUID) {
}