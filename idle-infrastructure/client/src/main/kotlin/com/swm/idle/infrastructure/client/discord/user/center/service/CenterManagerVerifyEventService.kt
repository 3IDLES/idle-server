package com.swm.idle.infrastructure.client.discord.user.center.service

import com.swm.idle.domain.user.center.event.CenterManagerVerifyEvent
import com.swm.idle.infrastructure.client.discord.common.event.EventType
import com.swm.idle.infrastructure.client.discord.common.properties.DiscordClientProperties
import com.swm.idle.infrastructure.client.discord.common.utils.DiscordMessageClient
import com.swm.idle.infrastructure.client.discord.common.vo.DiscordMessage
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import java.net.URI

@Component
class CenterManagerVerifyEventService(
    private val discordMessageClient: DiscordMessageClient,
    private val discordClientProperties: DiscordClientProperties,
) {

    private val logger = KotlinLogging.logger { }

    fun sendVerifyMessage(centerManagerVerifyEvent: CenterManagerVerifyEvent) {

        if (this.discordClientProperties.events.getValue(EventType.CENTER_MANAGER_VERIFICATION).active) {
            val discordUri =
                URI(this.discordClientProperties.events.getValue(EventType.CENTER_MANAGER_VERIFICATION).url)
            val message =
                "[🌟새로운 센터 관리자 ${centerManagerVerifyEvent.centerManager.name} 님이 관리자 인증 요청을 보냈어요! 빠르게 확인해주세요 :)"

            runCatching {
                discordMessageClient.send(
                    uri = discordUri,
                    message = DiscordMessage(message),
                )
            }.onFailure {
                logger.error(it) { "💥센터 관리자 인증 요청 발생! 디스코드 메세지 전송에 실패했습니다." }
            }
        }
    }

}
