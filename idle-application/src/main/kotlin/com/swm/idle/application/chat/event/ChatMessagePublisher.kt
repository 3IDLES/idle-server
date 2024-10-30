package com.swm.idle.application.chat.event

import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class ChatMessagePublisher(
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun publish(chatMessage: ChatMessage) {
        eventPublisher.publishEvent(chatMessage)
    }

}
