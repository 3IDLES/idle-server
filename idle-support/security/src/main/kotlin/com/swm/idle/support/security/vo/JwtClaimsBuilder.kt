package com.swm.idle.support.security.vo

import java.time.Instant
import java.util.*

@DslMarker
annotation class JwtClaimsDsl

@JwtClaimsDsl
class JwtClaimsBuilder {
    private var registeredClaims = JwtClaims.RegisteredClaims()
    private val customClaims = mutableMapOf<String, Any>()

    fun registeredClaims(init: RegisteredClaimsBuilder.() -> Unit) {
        val builder = RegisteredClaimsBuilder().apply(init)
        registeredClaims = builder.build()
    }

    fun customClaims(block: MutableMap<String, Any>.() -> Unit) {
        customClaims.apply(block)
    }

    fun build(): JwtClaims = JwtClaims(registeredClaims, customClaims)
}


@JwtClaimsDsl
class RegisteredClaimsBuilder {
    var sub: String? = null
    var exp: Date? = Date.from(Instant.now().plusSeconds(JwtClaims.DEFAULT_EXPIRATION_TIME_SEC))
    var iat: Date? = Date.from(Instant.now())
    var nbf: Date? = Date.from(Instant.now())
    var iss: String? = null
    var aud: List<String>? = null
    var jti: String? = null

    fun build(): JwtClaims.RegisteredClaims =
        JwtClaims.RegisteredClaims(sub, exp, iat, nbf, iss, aud, jti)
}

fun JwtClaims(init: JwtClaimsBuilder.() -> Unit): JwtClaims = JwtClaimsBuilder().apply(init).build()

