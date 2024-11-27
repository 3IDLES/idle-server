package com.swm.idle.presentation.chat.controller

import com.swm.idle.application.chat.facade.ChatMessageFacadeService
import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller

@Controller
class ChatMessageHandler(
    private val simpMessageSendingOperations: SimpMessageSendingOperations,
    private val chatMessageFacadeService: ChatMessageFacadeService,
) {

    private val logger = KotlinLogging.logger { }

    @EventListener
    fun handleChatMessage(chatMessage: ChatMessage) {
        logger.info { "Handler까지 도달 " }

        simpMessageSendingOperations.convertAndSend(
            "/topic/chat-rooms/${chatMessage.roomId}",
            chatMessage
        )

        chatMessageFacadeService.saveMessage(chatMessage)
    }

}
