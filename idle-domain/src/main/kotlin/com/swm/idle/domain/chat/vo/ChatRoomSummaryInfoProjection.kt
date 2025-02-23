package com.swm.idle.domain.chat.vo

import java.time.LocalDateTime

interface ChatRoomSummaryInfoProjection {
    fun getChatRoomId(): ByteArray
    fun getOpponentId(): ByteArray
    fun getUnreadCount(): Int
    fun getLastMessage(): String
    fun getLastMessageTime(): LocalDateTime
}