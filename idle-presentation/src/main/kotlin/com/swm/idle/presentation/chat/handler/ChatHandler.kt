package com.swm.idle.presentation.chat.handler

import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import com.swm.idle.domain.chat.vo.ReadMessage
import com.swm.idle.support.transfer.chat.ChatMessageResponse
import com.swm.idle.support.transfer.chat.ReadNoti
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller

@Controller
class ChatHandler(
    private val messageTemplate: SimpMessageSendingOperations,
) {
    @EventListener
    fun handleSendMessage(sendMessage: ChatMessage) {
        messageTemplate.convertAndSend("/sub/${sendMessage.receiverId}", ChatMessageResponse(sendMessage))
    }

    @EventListener
    fun handleReadMessage(readMessage: ReadMessage) {
        messageTemplate.convertAndSend("/sub/${readMessage.receiverId}", ReadNoti(readMessage))
    }
}