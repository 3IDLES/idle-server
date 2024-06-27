package com.swm.idle.api.auth.center.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Center Login API",
    description = "센터 로그인 API"
)
data class LoginRequest(
    val identifier: String,
    val password: String,
)
