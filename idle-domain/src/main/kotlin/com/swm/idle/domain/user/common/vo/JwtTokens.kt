package com.swm.idle.domain.user.common.vo

data class JwtTokens(
    val accessToken: String,
    val refreshToken: String,
)
