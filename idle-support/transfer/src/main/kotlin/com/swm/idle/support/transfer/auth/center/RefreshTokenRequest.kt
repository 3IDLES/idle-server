package com.swm.idle.support.transfer.auth.center

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Refresh Token Request",
    description = "Refresh Token 요청"
)
data class RefreshTokenRequest(
    @JsonProperty("refreshToken")
    @Schema(description = "Refresh Token")
    val refreshToken: String,
)
