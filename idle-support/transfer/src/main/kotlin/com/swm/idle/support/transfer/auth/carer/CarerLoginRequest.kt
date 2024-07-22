package com.swm.idle.support.transfer.auth.carer

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Carer Login Request",
    description = "요양 보호사 로그인 요청"
)
data class CarerLoginRequest(
    @Schema(description = "핸드폰 번호", example = "010-0000-0000")
    val phoneNumber: String,
    @Schema(description = "인증 번호", example = "123456")
    val verificationNumber: String,
)
