package com.swm.idle.domain.chat.event

import com.swm.idle.domain.chat.config.ChatMessageRedisConfig
import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class ChatMessageRedisPublisher(
    private val redisTemplate: RedisTemplate<String, Any>,
) {

    private val logger = KotlinLogging.logger {}

    fun publish(chatMessage: ChatMessage) {
        logger.info { "RedisPublisher 도달 " }

        redisTemplate.convertAndSend(ChatMessageRedisConfig.CHAT_MESSAGE, chatMessage)
    }

}
