package com.swm.idle.presentation.common.security.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.swm.idle.presentation.common.exception.ErrorResponse
import com.swm.idle.support.security.exception.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class JwtTokenExpiredHandleFilter(
    private val objectMapper: ObjectMapper,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (exception: JwtException.TokenExpired) {
            handleJwtTokenException(
                response = response,
                exception = exception,
                message = "토큰이 만료되었습니다.",
            )
        } catch (exception: JwtException.TokenDecodeException) {
            handleJwtTokenException(
                response = response,
                exception = exception,
                message = "유효하지 않은 토큰입니다.",
            )
        }
    }

    private fun handleJwtTokenException(
        response: HttpServletResponse,
        exception: JwtException,
        message: String,
    ) {
        val errorResponse = ErrorResponse(
            code = exception.code,
            message = message,
        )

        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType =
            "${MediaType.APPLICATION_JSON_VALUE};charset=${Charsets.UTF_8.name()}"
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }

}
