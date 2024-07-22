package com.swm.idle.application.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(value = "auth.jwt")
data class JwtTokenProperties(
    val issuer: String,
    val access: TokenProperties,
    val refresh: TokenProperties,
) {

    data class TokenProperties(
        val carer: ExpirationPolicy,
        val center: ExpirationPolicy,
        val secret: String,
    )

    data class ExpirationPolicy(
        val expireSeconds: Long,
    )
}

