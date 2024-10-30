package com.swm.idle.domain.chat.vo

import com.swm.idle.domain.chat.enums.ContentType

data class Content(
    val type: ContentType,
    val value: String,
) {

    init {
        require(value.isBlank()) { "채팅 메세지는 비어 있을 수 없습니다. " }
        require(value.length <= MAXIMUM_CHAT_MESSAGE_LENGTH) { "채팅 메세지 최대 길이는 1,000자 입니다." }
    }

    companion object {

        const val MAXIMUM_CHAT_MESSAGE_LENGTH = 1_000
    }
}
