package com.swm.idle.presentation.chat.controller

import com.swm.idle.application.chat.facade.ChatFacadeService
import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import com.swm.idle.domain.chat.vo.ChatRoomSummaryInfo
import com.swm.idle.presentation.chat.api.ChatCenterApi
import com.swm.idle.support.transfer.chat.CreateChatRoomRequest
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class ChatCenterController(
    private val chatMessageService: ChatFacadeService,
) : ChatCenterApi {

    override fun createChatroom(request: CreateChatRoomRequest) {
        chatMessageService.createChatroom(request, false)
    }

    override fun centerChatroomSummary(): List<ChatRoomSummaryInfo> {
        return chatMessageService.getChatroomSummary(false)
    }

    override fun recentMessages(chatroomId: UUID, messageId: UUID?): List<ChatMessage> {
        return chatMessageService.getRecentMessages(chatroomId, messageId)
    }
}