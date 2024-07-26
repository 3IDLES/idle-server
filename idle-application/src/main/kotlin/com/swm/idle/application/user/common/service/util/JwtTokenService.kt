package com.swm.idle.application.user.common.service.util

import com.swm.idle.application.common.properties.JwtTokenProperties
import com.swm.idle.application.user.vo.UserTokenClaims
import com.swm.idle.domain.user.carer.entity.jpa.Carer
import com.swm.idle.domain.user.center.entity.jpa.CenterManager
import com.swm.idle.domain.user.common.entity.jpa.User
import com.swm.idle.domain.user.common.entity.redis.UserRefreshTokenRedisHash
import com.swm.idle.domain.user.common.enum.UserTokenType
import com.swm.idle.domain.user.common.enum.UserType
import com.swm.idle.domain.user.common.repository.redis.UserRefreshTokenRepository
import com.swm.idle.domain.user.common.vo.PhoneNumber
import com.swm.idle.support.security.exception.JwtException
import com.swm.idle.support.security.util.JwtTokenProvider
import com.swm.idle.support.security.vo.JwtClaims
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class JwtTokenService(
    private val jwtTokenProperties: JwtTokenProperties,
    private val userRefreshTokenRepository: UserRefreshTokenRepository,
) {

    fun generateAccessToken(user: User): String {
        val (expireSeconds, userType) = when (user) {
            is CenterManager -> jwtTokenProperties.access.center.expireSeconds to UserType.CENTER
            is Carer -> jwtTokenProperties.access.carer.expireSeconds to UserType.CARER
            else -> throw IllegalArgumentException(NOT_SUPPORTED_USER_TYPE)
        }

        val jwtClaims = JwtClaims {
            registeredClaims {
                iss = jwtTokenProperties.issuer
                exp = Date.from(Instant.now().plusSeconds(expireSeconds))
            }
            customClaims {
                this["type"] = UserTokenType.ACCESS_TOKEN.name
                this["userId"] = user.id.toString()
                this["phoneNumber"] = user.phoneNumber
                this["userType"] = userType.value
            }
        }

        return JwtTokenProvider.createToken(jwtClaims, jwtTokenProperties.access.secret)
    }

    fun generateRefreshToken(user: User): String {
        val (expireSeconds, userType) = when (user) {
            is CenterManager -> jwtTokenProperties.refresh.center.expireSeconds to UserType.CENTER
            is Carer -> jwtTokenProperties.refresh.carer.expireSeconds to UserType.CARER
            else -> throw JwtException.NotSupportUserTokenType()
        }

        val jwtClaims = JwtClaims {
            registeredClaims {
                iss = jwtTokenProperties.issuer
                exp = Date.from(Instant.now().plusSeconds(expireSeconds))
            }
            customClaims {
                this["type"] = UserTokenType.REFRESH_TOKEN.name
                this["userId"] = user.id.toString()
                this["userType"] = userType.value
            }
        }

        return JwtTokenProvider
            .createToken(jwtClaims, jwtTokenProperties.refresh.secret)
            .also {
                userRefreshTokenRepository.save(
                    UserRefreshTokenRedisHash(
                        id = user.id,
                        refreshToken = it,
                        userType = userType.value,
                        expireSeconds = expireSeconds,
                    )
                )
            }
    }

    fun resolveAccessToken(accessToken: String): UserTokenClaims.AccessToken {
        val jwtClaims: JwtClaims = JwtTokenProvider
            .verifyToken(accessToken, jwtTokenProperties.access.secret)
            .getOrThrow()

        return UserTokenClaims.AccessToken(
            userId = UUID.fromString(jwtClaims.customClaims["userId"] as String),
            phoneNumber = PhoneNumber(jwtClaims.customClaims["phoneNumber"] as String),
        )
    }

    fun resolveRefreshToken(refreshToken: String): UserTokenClaims.RefreshToken {
        val jwtClaims: JwtClaims = JwtTokenProvider
            .verifyToken(refreshToken, jwtTokenProperties.refresh.secret)
            .getOrThrow()

        return UserTokenClaims.RefreshToken(
            userId = UUID.fromString(jwtClaims.customClaims["userId"] as String),
        )
    }

    companion object {

        const val NOT_SUPPORTED_USER_TYPE = "지원되지 않는 유저 타입입니다."
    }

}
