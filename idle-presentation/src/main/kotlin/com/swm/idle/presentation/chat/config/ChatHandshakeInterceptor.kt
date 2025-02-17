package com.swm.idle.presentation.chat.config

import com.swm.idle.application.common.properties.JwtTokenProperties
import com.swm.idle.support.security.util.JwtTokenProvider
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

@Component
class ChatHandshakeInterceptor(
    private val jwtTokenProperties: JwtTokenProperties,
    ): HandshakeInterceptor {

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        if (request is ServletServerHttpRequest) {
            val accessToken = request.servletRequest.getHeader("Authorization")

            val claims = try {
                JwtTokenProvider.verifyToken(accessToken, jwtTokenProperties.access.secret).getOrThrow()
            } catch (e: Exception) {
                return false
            }

            attributes["userId"] = claims.customClaims["userId"] as String
            return true
        }
        return false
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?
    ) {
        // No-option
    }
}