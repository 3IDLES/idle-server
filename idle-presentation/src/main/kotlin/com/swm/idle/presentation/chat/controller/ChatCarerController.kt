package com.swm.idle.presentation.chat.controller

import com.swm.idle.application.chat.facade.ChatFacadeService
import com.swm.idle.domain.chat.vo.ChatRoomSummaryInfo
import com.swm.idle.presentation.chat.api.ChatCarerApi
import com.swm.idle.support.transfer.chat.ChatMessageResponse
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

    /**
     * Retrieves a list of chat room summaries for the carer.
     *
     * @return A list of chat room summary information relevant to the carer.
     */
    override fun carerChatroomSummary(): List<ChatRoomSummaryInfo> {
        return chatMessageService.getChatroomSummary(true)
    }

    /**
     * Retrieves recent chat messages for a specific chatroom, optionally starting from a given message ID.
     *
     * @param chatroomId The unique identifier of the chatroom.
     * @param messageId The ID of the message to start retrieving from, or null to retrieve the most recent messages.
     * @return A response containing recent chat messages for the specified chatroom.
     */
    override fun recentMessages(chatroomId: UUID, messageId: UUID?): ChatMessageResponse {
        return chatMessageService.getRecentMessages(chatroomId, messageId, true)
    }
}