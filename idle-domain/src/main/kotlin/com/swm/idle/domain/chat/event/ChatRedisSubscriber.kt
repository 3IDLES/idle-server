package com.swm.idle.domain.chat.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import com.swm.idle.domain.chat.vo.ReadMessage
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class ChatRedisSubscriber(
    private val eventPublisher: ApplicationEventPublisher,
    private val objectMapper: ObjectMapper,
    private val redisTemplate: RedisTemplate<String, Any>
) : MessageListener {

    override fun onMessage(message: Message, pattern: ByteArray?) {
        val rawJson = redisTemplate.stringSerializer.deserialize(message.body)
        val actualJson = objectMapper.readTree(rawJson).asText()
        val jsonNode = objectMapper.readTree(actualJson)

        when (jsonNode.get(ChatRedisPublisher.TYPE).asText()) {
            ChatRedisPublisher.SEND_MESSAGE -> {
                val chatMessage: ChatMessage = objectMapper.treeToValue(
                    jsonNode.get(ChatRedisPublisher.DATA), ChatMessage::class.java
                )

                eventPublisher.publishEvent(chatMessage)
            }
            ChatRedisPublisher.READ_MESSAGE -> {
                val readMessage: ReadMessage = objectMapper.treeToValue(
                    jsonNode.get(ChatRedisPublisher.DATA), ReadMessage::class.java
                )

                eventPublisher.publishEvent(readMessage)
            }
        }
    }
}