package com.swm.idle.presentation.chat.api

import com.swm.idle.domain.chat.vo.ChatRoomSummaryInfo
import com.swm.idle.presentation.common.security.annotation.Secured
import com.swm.idle.support.transfer.chat.ChatMessageResponse
import com.swm.idle.support.transfer.chat.CreateChatRoomRequest
import com.swm.idle.support.transfer.chat.CreateChatRoomResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@Tag(name = "Chat - Center", description = "채팅 API")
@RequestMapping("/api/v2/chat/center/", produces = ["application/json;charset=utf-8"])
interface ChatCenterApi {

    @Secured
    @Operation(summary = "센터장의 채팅방 개설 API")
    @PostMapping("/chatrooms")
    @ResponseStatus(HttpStatus.OK)
    fun createChatroom(request: CreateChatRoomRequest): CreateChatRoomResponse

    /**
                        * Retrieves the most recent chat message for the specified chat room, optionally starting from a given message ID.
                        *
                        * @param chatroomId The unique identifier of the chat room.
                        * @param messageId An optional message ID to specify the starting point for retrieval.
                        * @return The most recent chat message in the chat room.
                        */
                       @Secured
    @Operation(summary = "센터장의 최근 채팅 메시지 조회 API")
    @GetMapping("/chatrooms/{chatroom-id}/messages")
    @ResponseStatus(HttpStatus.OK)
    fun recentMessages(@PathVariable(value = "chatroom-id") chatroomId: UUID,
                       @RequestParam(value = "message-id", required = false) messageId: UUID?): ChatMessageResponse

        @Secured
    @Operation(summary = "센터장의 채팅방 요약 목록 조회 API")
    @GetMapping("/chatrooms")
    @ResponseStatus(HttpStatus.OK)
    fun centerChatroomSummary(): List<ChatRoomSummaryInfo>
}
