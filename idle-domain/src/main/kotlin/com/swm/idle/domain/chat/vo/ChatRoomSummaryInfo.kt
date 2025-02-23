package com.swm.idle.domain.chat.vo

import java.nio.ByteBuffer
import java.time.LocalDateTime
import java.util.UUID

data class ChatRoomSummaryInfo(
    val chatRoomId: UUID,
    val lastMessage: String,
    val lastMessageTime: LocalDateTime,
    val count: Int,
    val opponentId: UUID,
    var opponentName: String,
    var opponentProfileImageUrl: String?,
) {
    constructor(chatRoomId: ByteArray,
                lastMessage: String,
                lastMessageTime: LocalDateTime,
                count: Int,
                receiverId: ByteArray,
        ) : this(
        fromByteArray(chatRoomId),
        lastMessage,
        lastMessageTime,
        count,
        fromByteArray(receiverId),
        "알 수 없음",
        null,
    )

    companion object {
        fun fromByteArray(array: ByteArray): UUID {
            val buffer = ByteBuffer.wrap(array)
            val mostSigBits = buffer.getLong()
            val leastSigBits = buffer.getLong()
            return UUID(mostSigBits, leastSigBits)
        }
    }
}