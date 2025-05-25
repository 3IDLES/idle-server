package com.swm.idle.domain.chat.vo

import java.time.LocalDateTime

interface ChatRoomSummaryInfoProjection {
    fun getChatRoomId(): ByteArray
    fun getCarerId(): ByteArray
    fun getCenterId(): ByteArray
    fun getLastMessage(): String
    fun getLastMessageTime(): LocalDateTime
    fun getLastSequence(): Long?
}