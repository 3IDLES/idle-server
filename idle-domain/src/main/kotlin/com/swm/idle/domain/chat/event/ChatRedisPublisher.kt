package com.swm.idle.domain.chat.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.swm.idle.domain.chat.config.ChatRedisConfig
import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import com.swm.idle.domain.chat.vo.ReadMessage
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class ChatRedisPublisher(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val objectMapper: ObjectMapper
) {

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
