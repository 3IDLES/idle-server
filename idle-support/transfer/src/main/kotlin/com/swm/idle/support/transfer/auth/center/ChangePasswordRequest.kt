package com.swm.idle.support.transfer.auth.center

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "ChangePasswordRequest",
    description = "센터 관리자 비밀번호 변경 요청"
)
data class ChangePasswordRequest(
    @Schema(description = "신규 비밀번호")
    val newPassword: String,
)
