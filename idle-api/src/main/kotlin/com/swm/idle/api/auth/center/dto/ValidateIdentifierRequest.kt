package com.swm.idle.api.auth.center.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Center Manager Identifier Check Duplication Request",
    description = "센터 관리자 아이디 체크 "
)
data class ValidateIdentifierRequest(
    @Schema(description = "아이디(ID)")
    val identifier: String,
)
