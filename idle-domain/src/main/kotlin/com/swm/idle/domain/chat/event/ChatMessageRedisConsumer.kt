package com.swm.idle.domain.chat.event

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Component

@Component
class ChatMessageRedisConsumer(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val objectMapper: ObjectMapper,
) : MessageListener {

    val logger = KotlinLogging.logger {}

    override fun onMessage(
        message: Message,
        pattern: ByteArray?,
    ) {
        logger.debug { "Received message: $message" }

        objectMapper.readValue<ChatMessage>(message.body)
            .also { applicationEventPublisher.publishEvent(it) }
    }

}
