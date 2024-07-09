package com.swm.idle.support.security.jwt.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.swm.idle.support.security.jwt.vo.JwtClaims

object JwtTokenProvider {

    fun createToken(
        jwtClaims: JwtClaims,
        secret: String,
    ): String {
        return JWT.create().withJWTId(jwtClaims.jti).withSubject(jwtClaims.sub)
            .withIssuer(jwtClaims.iss).withAudience(*jwtClaims.aud?.toTypedArray() ?: arrayOf())
            .withIssuedAt(jwtClaims.iat).withNotBefore(jwtClaims.nbf).withExpiresAt(jwtClaims.exp)
            .withPayload(jwtClaims.customClaims).sign(Algorithm.HMAC256(secret))
    }

}
