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

    @Secured
    @Operation(summary = "센터장의 최근 채팅 메시지 조회 API")
    @GetMapping("/chatrooms/{chatroom-id}/messages")
    @ResponseStatus(HttpStatus.OK)
    fun recentMessages(@PathVariable(value = "chatroom-id") chatroomId: UUID,
                       @RequestParam(value = "message-id", required = false) messageId: UUID?): List<ChatMessageResponse>

        @Secured
    @Operation(summary = "센터장의 채팅방 요약 목록 조회 API")
    @GetMapping("/chatrooms")
    @ResponseStatus(HttpStatus.OK)
    fun centerChatroomSummary(): List<ChatRoomSummaryInfo>

    @Secured
    @Operation(summary = "센터장의 단일 채팅방 정보 조회 API")
    @GetMapping("/chatrooms/{chatroom-id}/opponent/{opponent-id}")
    @ResponseStatus(HttpStatus.OK)
    fun carerSingleChatroomSummary(@PathVariable(value = "chatroom-id") chatroomId: UUID,
                                   @PathVariable(value = "opponent-id") opponentId: UUID): ChatRoomSummaryInfo
}
