package com.swm.idle.application.user.common.service.domain

import com.swm.idle.application.user.center.service.domain.CenterManagerService
import com.swm.idle.application.user.common.service.util.JwtTokenService
import com.swm.idle.application.user.vo.UserTokenClaims
import com.swm.idle.domain.user.common.repository.redis.UserRefreshTokenRepository
import com.swm.idle.domain.user.common.vo.JwtTokens
import com.swm.idle.support.security.exception.JwtException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class RefreshTokenService(
    private val jwtTokenService: JwtTokenService,
    private val userRefreshTokenRepository: UserRefreshTokenRepository,
    private val centerManagerService: CenterManagerService,
) {

    @Transactional
    fun create(refreshToken: String): JwtTokens {
        lateinit var token: UserTokenClaims.RefreshToken

        try {
            token = jwtTokenService.resolveRefreshToken(refreshToken)
        } catch (e: JwtException.TokenExpired) {
            throw JwtException.TokenNotValid()
        }

        validateRefreshToken(token)

        val centerManager = centerManagerService.getById(token.userId)

        return JwtTokens(
            accessToken = jwtTokenService.generateAccessToken(centerManager),
            refreshToken = jwtTokenService.generateRefreshToken(centerManager)
        )
    }

    private fun validateRefreshToken(token: UserTokenClaims.RefreshToken) {
        if (userRefreshTokenRepository.existsById(token.userId).not()) {
            throw JwtException.TokenNotFound()
        }
    }

    @Transactional
    fun delete(userId: UUID) {
        userRefreshTokenRepository.deleteById(userId)
    }

}
