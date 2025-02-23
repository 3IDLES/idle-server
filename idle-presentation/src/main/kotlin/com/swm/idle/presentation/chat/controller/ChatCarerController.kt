package com.swm.idle.presentation.chat.controller

import com.swm.idle.application.chat.facade.ChatFacadeService
import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import com.swm.idle.domain.chat.vo.ChatRoomSummaryInfo
import com.swm.idle.presentation.chat.api.ChatCarerApi
import com.swm.idle.support.transfer.chat.CreateChatRoomRequest
import com.swm.idle.support.transfer.chat.CreateChatRoomResponse
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class ChatCarerController(
    private val chatMessageService: ChatFacadeService,
) : ChatCarerApi {

    override fun createChatroom(request: CreateChatRoomRequest): CreateChatRoomResponse {
        return chatMessageService.createChatroom(request,true)
    }

    override fun carerChatroomSummary(): List<ChatRoomSummaryInfo> {
        return chatMessageService.getChatroomSummary(true)
    }

    override fun recentMessages(chatroomId: UUID, messageId: UUID?): List<ChatMessage> {
        return chatMessageService.getRecentMessages(chatroomId, messageId)
    }
}