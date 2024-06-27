package com.swm.idle.api.auth.center.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Refresh Token Request",
    description = "Refresh Token 요청"
)
data class RefreshTokenRequest(
    @Schema(description = "Refresh Token")
    val refreshToken: String,
)
