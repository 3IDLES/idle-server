package com.swm.idle.support.transfer.auth.center

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Center Login API",
    description = "센터 로그인 API"
)
data class CenterLoginRequest(
    @Schema(description = "아이디(ID)")
    val identifier: String,
    @Schema(description = "비밀번호")
    val password: String,
)
