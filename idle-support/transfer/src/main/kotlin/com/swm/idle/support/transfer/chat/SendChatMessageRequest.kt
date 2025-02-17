package com.swm.idle.support.transfer.chat

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty


data class SendChatMessageRequest@JsonCreator constructor(
    @JsonProperty("chatroomId") val chatroomId: String,
    @JsonProperty("content") val content: String,
    @JsonProperty("senderName") val senderName: String,
    @JsonProperty("receiverId") val receiverId: String
)
