package com.swm.idle.api.auth.center.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Send Verification Message Request",
    description = "전화번호 인증 메세지 요청"
)
data class SendVerificationMessageRequest(
    @Schema(description = "Phone Number")
    val phoneNumber: String,
)
