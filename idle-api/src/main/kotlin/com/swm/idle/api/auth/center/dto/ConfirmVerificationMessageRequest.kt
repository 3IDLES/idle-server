package com.swm.idle.api.auth.center.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Confirm Verification Number Request",
    description = "인증 번호 검증 요청"
)
data class ConfirmVerificationMessageRequest(
    @Schema(description = "Verification Number")
    val verificationNumber: String,
)

