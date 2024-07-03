package com.swm.idle.api

import com.swm.idle.infrastructure.sms.common.config.SmsConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(
    value = [
        SmsConfig::class,
    ]
)
class IdleServerApplication

fun main(args: Array<String>) {
    runApplication<IdleServerApplication>(*args)
}
