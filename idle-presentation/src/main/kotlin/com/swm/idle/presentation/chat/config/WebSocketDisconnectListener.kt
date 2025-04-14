package com.swm.idle.presentation.chat.config

import com.swm.idle.domain.chat.event.ChatRedisTemplate
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class WebSocketDisconnectListener(
    private val redisTemplate: ChatRedisTemplate
) {

    @EventListener
    fun handleDisconnect(event: SessionDisconnectEvent) {
        val headers = StompHeaderAccessor.wrap(event.message)
        val userId = headers.sessionAttributes?.get("userId") as? String

        if (userId != null) {
            redisTemplate.delete(userId)
        }
    }
}
