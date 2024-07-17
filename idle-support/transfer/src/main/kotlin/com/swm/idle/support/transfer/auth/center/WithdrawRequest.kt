package com.swm.idle.support.transfer.auth.center

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Center Withdraw Request API",
    description = "센터 회원 탈퇴 API"
)
data class WithdrawRequest(
    @Schema(description = "회원 탈퇴 사유")
    val reason: String,
    @Schema(description = "회원 비밀번호")
    val password: String,
)
