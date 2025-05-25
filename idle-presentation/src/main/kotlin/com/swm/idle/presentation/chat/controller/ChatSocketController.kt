package com.swm.idle.presentation.chat.controller

import com.swm.idle.application.chat.facade.ChatFacadeService
import com.swm.idle.support.transfer.chat.ReadChatMessageRequest
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

    @MessageMapping("/send/carer")
    fun carerSendMessage(@Payload request: SendChatMessageRequest,
                             headerAccessor: SimpMessageHeaderAccessor) {
        val userId = UUID.fromString(headerAccessor.sessionAttributes?.get("userId") as String)
        chatMessageService.send(request, userId, true)
    }

    @MessageMapping("/read/carer")
    fun carerRead(@Payload request: ReadChatMessageRequest,
                  headerAccessor: SimpMessageHeaderAccessor) {
        val userId = UUID.fromString(headerAccessor.sessionAttributes?.get("userId") as String)
        chatMessageService.read(request, userId, true)
    }

    @MessageMapping("/send/center")
    fun centerSendMessage(@Payload request: SendChatMessageRequest,
                    headerAccessor: SimpMessageHeaderAccessor) {
        val userId = UUID.fromString(headerAccessor.sessionAttributes?.get("userId") as String)
        chatMessageService.send(request, userId, false)
    }

    @MessageMapping("/read/center")
    fun centerRead(@Payload request: ReadChatMessageRequest,
                   headerAccessor: SimpMessageHeaderAccessor) {
        val userId = UUID.fromString(headerAccessor.sessionAttributes?.get("userId") as String)
        chatMessageService.read(request, userId, false)
    }
}