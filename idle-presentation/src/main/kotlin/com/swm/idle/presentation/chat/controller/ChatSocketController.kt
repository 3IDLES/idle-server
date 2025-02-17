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

    @MessageMapping("/send")
    fun sendMessage(@Payload request: SendChatMessageRequest,
                             headerAccessor: SimpMessageHeaderAccessor) {
        val userId = UUID.fromString(headerAccessor.sessionAttributes?.get("userId") as String)
        chatMessageService.sendMessage(request, userId)
    }

    @MessageMapping("/read")
    fun read(@Payload request: ReadChatMessagesReqeust,
                      headerAccessor: SimpMessageHeaderAccessor) {
        val userId = UUID.fromString(headerAccessor.sessionAttributes?.get("userId") as String)
        chatMessageService.readMessage(request, userId)
    }
}