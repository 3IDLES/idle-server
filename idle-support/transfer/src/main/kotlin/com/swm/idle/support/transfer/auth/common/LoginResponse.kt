package com.swm.idle.support.transfer.auth.common

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Login Response - Success",
    description = "로그인 성공시 access token, refresh token 을 반환합니다",
)
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
)
