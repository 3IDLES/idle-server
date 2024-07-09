package com.swm.idle.domain.user.vo

data class JwtTokens(
    val accessToken: String,
    val refreshToken: String,
)
