package com.swm.idle.presentation.chat.controller

import com.swm.idle.application.chat.facade.ChatFacadeService
import com.swm.idle.domain.chat.vo.ChatRoomSummaryInfo
import com.swm.idle.presentation.chat.api.ChatCenterApi
import com.swm.idle.support.transfer.chat.ChatMessageResponse
import com.swm.idle.support.transfer.chat.CreateChatRoomRequest
import com.swm.idle.support.transfer.chat.CreateChatRoomResponse
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class ChatCenterController(
    private val chatMessageService: ChatFacadeService,
) : ChatCenterApi {

    override fun createChatroom(request: CreateChatRoomRequest): CreateChatRoomResponse {
        return chatMessageService.createChatroom(request,false)
    }

    /**
     * Retrieves summary information for all chat rooms in the center.
     *
     * @return A list of chat room summary information.
     */
    override fun centerChatroomSummary(): List<ChatRoomSummaryInfo> {
        return chatMessageService.getChatroomSummary(false)
    }

    /**
     * Retrieves recent chat messages for a specified chat room.
     *
     * @param chatroomId The unique identifier of the chat room.
     * @param messageId An optional message ID to fetch messages after this point.
     * @return A response containing recent chat messages for the given chat room.
     */
    override fun recentMessages(chatroomId: UUID, messageId: UUID?): ChatMessageResponse {
        return chatMessageService.getRecentMessages(chatroomId, messageId, false)
    }
}