package com.swm.idle.support.transfer.chat


data class ChatMessageResponse(
    val chatMessageInfos: List<ChatMessageInfo>,
    val opponentSequence: Long
)