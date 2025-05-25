package com.swm.idle.domain.chat.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.swm.idle.domain.chat.config.ChatRedisConfig
import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import com.swm.idle.domain.chat.vo.ReadMessage
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class ChatRedisTemplate(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val objectMapper: ObjectMapper
) {

    /**
     * Removes a chat room from the set of unread chat rooms for a specific receiver in Redis.
     *
     * This indicates that the receiver has no unread messages in the specified chat room.
     */
    fun removeUnreadChatRoom(receiverId: String, chatRoomId: UUID) {
        val key = "unread_chatroom:${receiverId}"
        redisTemplate.opsForSet().remove(key, chatRoomId.toString())
    }

    /**
     * Marks a chat room as having unread messages for a specific receiver by adding the chat room ID to the user's unread chat rooms set in Redis.
     *
     * @param receiverId The ID of the user who has unread messages.
     * @param chatRoomId The ID of the chat room to add to the unread set.
     */
    fun addUnreadChatRoom(receiverId: String, chatRoomId: String) {
        val key = "unread_chatroom:${receiverId}"
        redisTemplate.opsForSet().add(key, chatRoomId)
    }

    /**
     * Updates the read message sequence number for a user in a specific chat room in Redis.
     *
     * Stores the provided message sequence as the latest read position for the user within the chat room.
     */
    fun updateReadSequence(chatRoomId: String, messageSequence: String, userId: UUID) {
        val key = "chatroom_read_sequence:$chatRoomId:$userId"
        redisTemplate.opsForValue().set(key, messageSequence.toLong())
    }

    /**
     * Retrieves the read message sequence number for a user in a specific chat room from Redis.
     *
     * @return The last read message sequence number, or 0L if no value is found.
     */
    fun getReadSequence(userId: UUID, chatRoomId: UUID) : Long{
        val key = "chatroom_read_sequence:$chatRoomId:$userId"
        return redisTemplate.opsForValue().get(key)?.toString()?.toLong() ?: 0L
    }

    /**
     * Increments and returns the current message sequence number for the specified chat room.
     *
     * If the sequence does not exist, it is initialized to 1.
     *
     * @param chatRoomId The identifier of the chat room.
     * @return The incremented sequence number for the chat room.
     */
    fun getChatRoomSequence(chatRoomId: String): Long {
        val key = "chatroom_sequence:$chatRoomId"
        return redisTemplate.opsForValue().increment(key)?:1L
    }

    /**
     * Retrieves the set of chat room IDs with unread messages for the specified user.
     *
     * @param userId The unique identifier of the user.
     * @return A set of chat room IDs where the user has unread messages, or an empty set if none exist.
     */
    fun getUnreadChatRooms(userId: UUID): Set<String> {
        val key = "unread_chatroom:$userId"
        val members = redisTemplate.opsForSet().members(key) ?: return emptySet()
        return members.mapNotNull { it as? String }.toSet()
    }

    /**
     * Retrieves the read message sequence numbers for a user across multiple chat rooms.
     *
     * For each chat room ID, returns the last read message sequence number for the user.
     * If no sequence is found for a chat room, the value defaults to 0L.
     *
     * @param userId The user's unique identifier.
     * @param chatRoomIds The set of chat room IDs to query.
     * @return A map of chat room ID to the user's read message sequence number.
     */
    fun getReadSequences(userId: UUID, chatRoomIds: Set<String>): Map<String, Long> {
        if (chatRoomIds.isEmpty()) return emptyMap()

        val keys = chatRoomIds.map { chatRoomId -> "chatroom_read_sequence:$chatRoomId:$userId" }
        val values = redisTemplate.opsForValue().multiGet(keys) ?: emptyList()

        return keys.zip(values).associate { (key, value) ->
            val chatRoomId = key.split(":")[1]
            chatRoomId to (value as? Long ?: 0L)
        }
    }

    /**
     * Checks whether the user has an active chat session by verifying the existence of a corresponding Redis key.
     *
     * @return `true` if the user has an active chat session; `false` otherwise.
     */
    fun isChatting(userId: UUID): Boolean {
        return redisTemplate.hasKey(userId.toString())
    }

    fun delete(userId: String) {
        redisTemplate.delete(userId)
    }

    fun setSession(userId: String, duration: Duration) {
        redisTemplate.opsForValue().set(userId,"active",duration)
    }

    fun publish(chatMessage: ChatMessage) {
        val message = objectMapper.writeValueAsString(
            mapOf(TYPE to SEND_MESSAGE, DATA to chatMessage)
        )
        redisTemplate.convertAndSend(ChatRedisConfig.CHATROOM, message)
    }

    fun publish(readMessage: ReadMessage) {
        val message = objectMapper.writeValueAsString(
            mapOf(TYPE to READ_MESSAGE, DATA to readMessage)
        )
        redisTemplate.convertAndSend(ChatRedisConfig.CHATROOM, message)
    }

    companion object{
        const val TYPE = "ty"
        const val DATA = "dt"
        const val SEND_MESSAGE = "sm"
        const val READ_MESSAGE = "rm"
    }
}
