package com.swm.idle.presentation

import com.swm.idle.application.common.config.ApplicationConfig
import com.swm.idle.batch.common.config.BatchConfig
import com.swm.idle.domain.common.config.DomainConfig
import com.swm.idle.domain.common.config.RedisConfig
import com.swm.idle.infrastructure.aws.common.AwsConfig
import com.swm.idle.infrastructure.client.common.config.ClientConfig
import com.swm.idle.infrastructure.fcm.common.config.FcmConfig
import com.swm.idle.infrastructure.sms.common.config.SmsConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling
import java.util.*

@EnableScheduling
@SpringBootApplication
@Import(
    value = [
        ApplicationConfig::class,
        DomainConfig::class,
        RedisConfig::class,
        ClientConfig::class,
        SmsConfig::class,
        AwsConfig::class,
        BatchConfig::class,
        FcmConfig::class,
    ]
)
class IdleServerApplication {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
            runApplication<IdleServerApplication>(*args)
        }
    }
}
