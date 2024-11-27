package com.swm.idle.support.transfer.chat

import com.swm.idle.domain.chat.entity.jpa.ChatMessage

data class SendChatMessageRequest(
    val contents: List<ChatMessage.Content>,
)
