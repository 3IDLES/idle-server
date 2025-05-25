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
    private val redisTemplate: RedisTemplate<String, String>,
    private val objectMapper: ObjectMapper
) {

    fun removeUnreadChatRoom(receiverId: String, chatRoomId: UUID) {
        val key = "unread_chatroom:${receiverId}"
        redisTemplate.opsForSet().remove(key, chatRoomId.toString())
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
        return members.mapNotNull { it as? String }.toSet()
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
