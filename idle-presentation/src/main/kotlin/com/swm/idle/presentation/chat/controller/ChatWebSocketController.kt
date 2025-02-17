package com.swm.idle.presentation.chat.controller

import com.swm.idle.application.chat.facade.ChatMessageFacadeService
import com.swm.idle.support.common.uuid.UuidCreator
import com.swm.idle.support.transfer.chat.SendChatMessageRequest
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class ChatWebSocketController(
    private val chatMessageService: ChatMessageFacadeService,
) {

    @MessageMapping("/chat-rooms/{roomId}")
    @SendTo("/topic/chat-rooms/{roomId}")
    fun sendTextMessage(
        @DestinationVariable roomId: UUID,
        request: SendChatMessageRequest,
    ) {
        chatMessageService.sendTextMessage(
            roomId = roomId,
            senderId = UuidCreator.create(), // TODO: 토큰 인증 구현
            contents = request.contents,
        )
    }

}
