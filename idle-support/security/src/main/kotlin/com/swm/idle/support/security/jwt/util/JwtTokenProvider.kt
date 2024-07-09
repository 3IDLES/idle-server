package com.swm.idle.support.security.jwt.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.swm.idle.support.security.jwt.exception.JwtException
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

    fun verifyToken(
        token: String,
        secret: String,
    ): Result<JwtClaims> {
        val algorithm: Algorithm = Algorithm.HMAC256(secret)
        return verifyTokenWithAlgorithm(token, algorithm)
    }

    private fun verifyTokenWithAlgorithm(
        token: String,
        algorithm: Algorithm,
    ): Result<JwtClaims> {
        return runCatching {
            JWT.require(algorithm).build().verify(token)
        }.mapCatching { jwtClaims ->
            JwtClaims.from(jwtClaims)
        }.onFailure { exception ->
            handleException(exception)
        }
    }

    private fun handleException(exception: Throwable): Nothing {
        when (exception) {
            is JWTDecodeException -> throw JwtException.TokenDecodeException()
            is TokenExpiredException -> throw JwtException.TokenExpired()
            else -> throw JwtException.TokenNotValid()
        }
    }

}
