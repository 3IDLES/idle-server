package com.swm.idle.domain.user.util

import com.swm.idle.domain.center.entity.CenterManager
import com.swm.idle.domain.common.properties.JwtTokenProperties
import com.swm.idle.domain.user.common.enum.UserTokenType
import com.swm.idle.domain.user.entity.UserRefreshTokenRedisHash
import com.swm.idle.domain.user.repository.UserRefreshTokenRepository
import com.swm.idle.support.security.jwt.util.JwtTokenProvider
import com.swm.idle.support.security.jwt.vo.JwtClaims
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

}
