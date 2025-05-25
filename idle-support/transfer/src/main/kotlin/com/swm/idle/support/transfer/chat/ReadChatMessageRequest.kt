package com.swm.idle.support.transfer.chat

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ReadChatMessageRequest @JsonCreator constructor(
    @JsonProperty("chatroomId") val chatroomId: String,
    @JsonProperty("opponentId")val opponentId: String,
    @JsonProperty("sequence")val sequence: String
)