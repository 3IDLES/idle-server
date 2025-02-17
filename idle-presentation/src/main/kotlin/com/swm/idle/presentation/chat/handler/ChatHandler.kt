package com.swm.idle.presentation.chat.handler

import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import com.swm.idle.domain.chat.vo.ReadMessage
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller

@Controller
class ChatHandler(
    private val messageTemplate: SimpMessageSendingOperations,
) {
    @EventListener
    fun handleSendMessage(sendMessage: ChatMessage) {
        messageTemplate.convertAndSend("/sub/chatrooms/${sendMessage.chatRoomId}", sendMessage)
    }

    @EventListener
    fun handleReadMessage(raedMessage: ReadMessage) {
        messageTemplate.convertAndSend("/sub/chatrooms/${raedMessage.chatroomId}", raedMessage.readUserId)
    }
}