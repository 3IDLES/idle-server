package com.swm.idle.domain.chat.vo

import java.nio.ByteBuffer
import java.time.LocalDateTime
import java.util.UUID

data class ChatRoomSummaryInfo(
    val chatRoomId: UUID,
    val lastMessage: String,
    val lastMessageTime: LocalDateTime,
    var count: Long,
    var opponentId: UUID,
    var opponentName: String = "알 수 없음",
    var opponentProfileImageUrl: String? = null
) {
    constructor(
        chatRoomId: ByteArray,
        opponentId: ByteArray,
        lastMessage: String,
        lastMessageTime: LocalDateTime,
        count: Long
    ) : this(
        chatRoomId = fromByteArray(chatRoomId),
        opponentId =fromByteArray(opponentId),
        lastMessage = lastMessage,
        lastMessageTime = lastMessageTime,
        count= count,
    )

    companion object {
        fun fromByteArray(array: ByteArray): UUID {
            val buffer = ByteBuffer.wrap(array)
            val mostSigBits = buffer.long
            val leastSigBits = buffer.long
            return UUID(mostSigBits, leastSigBits)
        }
    }
}

