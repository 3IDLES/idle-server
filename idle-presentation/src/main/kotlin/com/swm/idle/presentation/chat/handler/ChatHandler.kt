package com.swm.idle.presentation.chat.handler

import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import com.swm.idle.domain.chat.vo.ReadMessage
import com.swm.idle.support.transfer.chat.ChatMessageSocketResponse
import com.swm.idle.support.transfer.chat.ReadNoti
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller

@Controller
class ChatHandler(
    private val messageTemplate: SimpMessageSendingOperations,
) {
    /**
     * Handles chat message events by sending the message to both the receiver and sender via WebSocket.
     *
     * Sends a `ChatMessageSocketResponse` containing the chat message to the appropriate subscription endpoints for real-time delivery.
     */
    @EventListener
    fun handleSendMessage(sendMessage: ChatMessage) {
        messageTemplate.convertAndSend("/sub/${sendMessage.receiverId}", ChatMessageSocketResponse(sendMessage))
        messageTemplate.convertAndSend("/sub/${sendMessage.senderId}", ChatMessageSocketResponse(sendMessage))
    }

    /**
     * Sends a read notification to both the receiver and the user who read the message via WebSocket.
     *
     * Listens for `ReadMessage` events and dispatches a `ReadNoti` to the appropriate WebSocket destinations for real-time notification.
     */
    @EventListener
    fun handleReadMessage(readMessage: ReadMessage) {
        messageTemplate.convertAndSend("/sub/${readMessage.receiverId}", ReadNoti(readMessage))
        messageTemplate.convertAndSend("/sub/${readMessage.readUserId}", ReadNoti(readMessage))
    }
}