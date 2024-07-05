package com.swm.idle.api.auth.core.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Confirm Verification Number Request",
    description = "인증 번호 검증 요청"
)
data class ConfirmSmsVerificationRequest(
    @Schema(description = "Phone Number", example = "010-0000-0000")
    val phoneNumber: String,

    @Schema(description = "Verification Number", example = "123456")
    val verificationNumber: String,
)

