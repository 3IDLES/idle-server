package com.swm.idle.support.transfer.chat

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class ReadChatMessagesReqeust @JsonCreator constructor(
    @JsonProperty("chatroomId") val chatroomId: UUID,
    @JsonProperty("opponentId")val opponentId: UUID
)