package com.swm.idle.domain.chat.vo

import com.swm.idle.domain.chat.enums.ContentType

data class Content(
    val type: ContentType,
    val value: String,
)
