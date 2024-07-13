package com.swm.idle.presentation.common.security.filter

import com.swm.idle.application.common.security.UserSecurityContext
import com.swm.idle.application.user.common.service.util.JwtTokenService
import com.swm.idle.application.user.vo.UserAuthentication
import com.swm.idle.support.security.context.SecurityContextHolder
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenService: JwtTokenService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        extractToken(request)?.let {
            processToken(it)
        }
        filterChain.doFilter(request, response)
        SecurityContextHolder.clearContext()
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val bearerToken: String? = request.getHeader(TOKEN_REQUEST_HEADER_KEY)
        return if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            bearerToken.substring(TOKEN_PREFIX.length)
        } else null
    }

    private fun processToken(token: String) {
        with(jwtTokenService.resolveAccessToken(token)) {
            UserAuthentication.from(this)
        }.let { userAuthentication ->
            UserSecurityContext(userAuthentication)
        }.also { securityContext ->
            SecurityContextHolder.setContext(securityContext)
        }
    }

    companion object {
        const val TOKEN_PREFIX = "Bearer "
        const val TOKEN_REQUEST_HEADER_KEY = "Authorization"
    }

}
