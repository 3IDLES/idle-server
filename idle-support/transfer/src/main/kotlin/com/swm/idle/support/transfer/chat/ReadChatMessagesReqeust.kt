package com.swm.idle.support.transfer.chat

import java.util.UUID

data class ReadChatMessagesReqeust(val chatroomId: UUID, val opponentId: UUID)