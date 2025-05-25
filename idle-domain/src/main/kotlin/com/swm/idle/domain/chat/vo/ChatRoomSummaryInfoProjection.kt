package com.swm.idle.domain.chat.vo

import java.time.LocalDateTime

interface ChatRoomSummaryInfoProjection {
    /**
 * Returns the unique identifier of the chat room as a byte array.
 */
fun getChatRoomId(): ByteArray
    /**
 * Returns the identifier of the carer associated with the chat room.
 *
 * @return A ByteArray representing the carer's unique identifier.
 */
fun getCarerId(): ByteArray
    /**
 * Returns the identifier of the center associated with the chat room.
 *
 * @return A ByteArray representing the center's unique identifier.
 */
fun getCenterId(): ByteArray
    /**
 * Returns the content of the last message sent in the chat room.
 *
 * @return The last message as a string.
 */
fun getLastMessage(): String
    /**
 * Returns the timestamp of the last message sent in the chat room.
 *
 * @return The date and time of the most recent message.
 */
fun getLastMessageTime(): LocalDateTime
    /**
 * Returns the sequence number of the last message in the chat room.
 *
 * @return The last message's sequence number.
 */
fun getLastSequence(): Long
}