package com.swm.idle.api.auth.common.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Send Verification Message Request",
    description = "전화번호 인증 메세지 요청"
)
data class SendVerificationMessageRequest(
    @JsonProperty("phoneNumber")
    @Schema(description = "Phone Number", example = "010-0000-0000")
    val phoneNumber: String,
)
