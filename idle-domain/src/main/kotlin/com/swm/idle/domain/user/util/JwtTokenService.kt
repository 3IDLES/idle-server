package com.swm.idle.domain.user.util

import com.swm.idle.domain.center.entity.CenterManager
import com.swm.idle.domain.common.properties.JwtTokenProperties
import com.swm.idle.domain.sms.vo.PhoneNumber
import com.swm.idle.domain.user.common.enum.UserTokenType
import com.swm.idle.domain.user.entity.UserRefreshTokenRedisHash
import com.swm.idle.domain.user.repository.UserRefreshTokenRepository
import com.swm.idle.domain.user.vo.UserTokenClaims
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

    fun generateAccessToken(centerManager: CenterManager): String {
        val jwtClaims = JwtClaims {
            registeredClaims {
                iss = jwtTokenProperties.issuer
                exp = Date.from(Instant.now().plusSeconds(jwtTokenProperties.access.expireSeconds))
            }
            customClaims {
                this["type"] = UserTokenType.ACCESS_TOKEN.name
                this["userId"] = centerManager.id.toString()
                this["phoneNumber"] = centerManager.phoneNumber
            }
        }

        return JwtTokenProvider.createToken(jwtClaims, jwtTokenProperties.access.secret)
    }

    fun generateRefreshToken(centerManager: CenterManager): String {
        val jwtClaims = JwtClaims {
            registeredClaims {
                iss = jwtTokenProperties.issuer
                exp = Date.from(Instant.now().plusSeconds(jwtTokenProperties.refresh.expireSeconds))
            }
            customClaims {
                this["type"] = UserTokenType.REFRESH_TOKEN.name
                this["userId"] = centerManager.id.toString()
            }
        }

        return JwtTokenProvider
            .createToken(jwtClaims, jwtTokenProperties.refresh.secret)
            .also {
                userRefreshTokenRepository.save(
                    UserRefreshTokenRedisHash(
                        id = centerManager.id,
                        refreshToken = it,
                        expireSeconds = jwtTokenProperties.refresh.expireSeconds
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

}
