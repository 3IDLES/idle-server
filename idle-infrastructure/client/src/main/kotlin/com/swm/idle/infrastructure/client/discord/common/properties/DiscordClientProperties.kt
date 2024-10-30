package com.swm.idle.infrastructure.client.discord.common.properties

import com.swm.idle.infrastructure.client.discord.common.event.EventType
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("client")
class DiscordClientProperties(
    val events: Map<EventType, Properties>,
) {

    data class Properties(
        val active: Boolean,
        val url: String,
    )
}
