package com.swm.idle.support.transfer.auth.common

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Confirm Verification Number Request",
    description = "인증 번호 검증 요청"
)
data class ConfirmSmsVerificationRequest(
    @Schema(description = "핸드폰 번호", example = "010-0000-0000")
    val phoneNumber: String,
    @Schema(description = "인증 번호", example = "123456")
    val verificationNumber: String,
)
