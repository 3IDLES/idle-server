package com.swm.idle.support.security.vo

import com.auth0.jwt.interfaces.DecodedJWT
import java.time.Instant
import java.util.*

data class JwtClaims(
    val registeredClaims: RegisteredClaims,
    val customClaims: MutableMap<String, Any> = mutableMapOf(),
) {

    data class RegisteredClaims(
        val sub: String? = null,
        val exp: Date? = Date.from(Instant.now().plusSeconds(DEFAULT_EXPIRATION_TIME_SEC)),
        val iat: Date? = Date.from(Instant.now()),
        val nbf: Date? = Date.from(Instant.now()),
        val iss: String? = null,
        val aud: List<String>? = null,
        val jti: String? = null,
    )

    val sub: String?
        get() = registeredClaims.sub
    val exp: Date?
        get() = registeredClaims.exp
    val iat: Date?
        get() = registeredClaims.iat
    val nbf: Date?
        get() = registeredClaims.nbf
    val iss: String?
        get() = registeredClaims.iss
    val aud: List<String>?
        get() = registeredClaims.aud
    val jti: String?
        get() = registeredClaims.jti

    companion object {

        const val DEFAULT_EXPIRATION_TIME_SEC: Long = 60 * 60

        fun from(claims: DecodedJWT): JwtClaims {
            val registeredClaims = RegisteredClaims(
                sub = claims.subject,
                exp = claims.expiresAt,
                iat = claims.issuedAt,
                nbf = claims.notBefore,
                iss = claims.issuer,
                aud = claims.audience,
                jti = claims.id,
            )
            val customClaims: MutableMap<String, Any> = mutableMapOf()
            claims.claims
                .filter { it.key !in registeredClaims::class.members.map { member -> member.name } }
                .forEach { (key, value) ->
                    customClaims[key] = value.`as`(Any::class.java)
                }
            return JwtClaims(registeredClaims, customClaims)
        }

    }

}
