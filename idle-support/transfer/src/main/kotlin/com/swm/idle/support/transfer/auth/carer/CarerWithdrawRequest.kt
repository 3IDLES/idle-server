package com.swm.idle.support.transfer.auth.carer

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Carer Withdraw Request",
    description = "요양 보호사 회원 탈퇴 요청"
)
data class CarerWithdrawRequest(
    @Schema(description = "회원 탈퇴 사유")
    val reason: String = "",
)
