package com.swm.idle.infrastructure.client.discord.common.utils

import com.swm.idle.infrastructure.client.discord.common.vo.DiscordMessage
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.net.URI

@FeignClient(
    name = "discord-message-client",
    url = "discord-webhook-url"
)
fun interface DiscordMessageClient {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun send(
        uri: URI,
        @RequestBody
        message: DiscordMessage,
    )

}
