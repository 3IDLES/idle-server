package com.swm.idle.domain.chat.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class ChatRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
) {

    fun removeUnreadChatRoom(receiverId: UUID, chatRoomId: String) {
        val key = "unread_chatroom:${receiverId}"
        redisTemplate.opsForSet().remove(key, chatRoomId)
    }

    fun addUnreadChatRoom(receiverId: String, chatRoomId: String) {
        val key = "unread_chatroom:${receiverId}"
        redisTemplate.opsForSet().add(key, chatRoomId)
    }

    fun updateReadSequence(chatRoomId: String, messageSequence: String, userId: UUID) {
        val key = "chatroom_read_sequence:$chatRoomId:$userId"
        redisTemplate.opsForValue().set(key, messageSequence)
    }

    fun getReadSequence(userId: UUID, chatRoomId: UUID) : Long{
        val key = "chatroom_read_sequence:$chatRoomId:$userId"
        return redisTemplate.opsForValue().get(key)?.toLong() ?: 0L
    }

    fun getChatRoomSequence(chatRoomId: String): Long {
        val key = "chatroom_sequence:$chatRoomId"
        return redisTemplate.opsForValue().increment(key)?:1L
    }

    fun getUnreadChatRooms(userId: UUID): Set<String> {
        val key = "unread_chatroom:$userId"
        val members = redisTemplate.opsForSet().members(key) ?: return emptySet()
        return members.mapNotNull { it }.toSet()
    }

    fun getReadSequences(userId: UUID, chatRoomIds: Set<String>): Map<String, Long> {
        if (chatRoomIds.isEmpty()) return emptyMap()

        val keys = chatRoomIds.map { chatRoomId -> "chatroom_read_sequence:$chatRoomId:$userId" }
        val values = redisTemplate.opsForValue().multiGet(keys) ?: emptyList()
        return keys.zip(values).associate { (key, value) ->
            val chatRoomId = key.split(":")[1]
            chatRoomId to (value?.toLongOrNull() ?: 0L)
        }
    }

    fun isChatting(userId: UUID): Boolean {
        return redisTemplate.hasKey(userId.toString())
    }

    fun delete(userId: String) {
        redisTemplate.delete(userId)
    }

    fun setSession(userId: String, duration: Duration) {
        redisTemplate.opsForValue().set(userId,"active",duration)
    }
}
