package com.swm.idle.presentation.chat.controller

import com.swm.idle.application.chat.facade.ChatFacadeService
import com.swm.idle.support.transfer.chat.ReadChatMessagesReqeust
import com.swm.idle.support.transfer.chat.SendChatMessageRequest
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class ChatSocketController (
    private val chatMessageService: ChatFacadeService,
) {

    /**
     * Handles chat message sending requests from carers via the "/send/carer" WebSocket endpoint.
     *
     * Extracts the carer's user ID from the WebSocket session and delegates message sending to the chat service.
     */
    @MessageMapping("/send/carer")
    fun carerSendMessage(@Payload request: SendChatMessageRequest,
                             headerAccessor: SimpMessageHeaderAccessor) {
        val userId = UUID.fromString(headerAccessor.sessionAttributes?.get("userId") as String)
        chatMessageService.send(request, userId, true)
    }

    /**
     * Marks chat messages as read for a carer user.
     *
     * Processes a read receipt request sent to the "/read/carer" WebSocket endpoint, identifying the user from the session and updating message read status as a carer.
     */
    @MessageMapping("/read/carer")
    fun carerRead(@Payload request: ReadChatMessagesReqeust,
                      headerAccessor: SimpMessageHeaderAccessor) {
        val userId = UUID.fromString(headerAccessor.sessionAttributes?.get("userId") as String)
        chatMessageService.read(request, userId, true)
    }

    /**
     * Handles chat message sending requests from center users via the "/send/center" WebSocket endpoint.
     *
     * Extracts the center user's ID from the WebSocket session and delegates the message sending to the chat message service.
     */
    @MessageMapping("/send/center")
    fun centerSendMessage(@Payload request: SendChatMessageRequest,
                    headerAccessor: SimpMessageHeaderAccessor) {
        val userId = UUID.fromString(headerAccessor.sessionAttributes?.get("userId") as String)
        chatMessageService.send(request, userId, false)
    }

    /**
     * Marks chat messages as read for a center user based on the provided request.
     *
     * @param request The request containing information about which messages to mark as read.
     */
    @MessageMapping("/read/center")
    fun centerRead(@Payload request: ReadChatMessagesReqeust,
             headerAccessor: SimpMessageHeaderAccessor) {
        val userId = UUID.fromString(headerAccessor.sessionAttributes?.get("userId") as String)
        chatMessageService.read(request, userId, false)
    }
}