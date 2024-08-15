package com.swm.idle.presentation

import com.swm.idle.application.common.config.ApplicationConfig
import com.swm.idle.batch.common.config.BatchConfig
import com.swm.idle.domain.common.config.DomainConfig
import com.swm.idle.domain.common.config.RedisConfig
import com.swm.idle.infrastructure.aws.common.AwsConfig
import com.swm.idle.infrastructure.client.common.config.ClientConfig
import com.swm.idle.infrastructure.sms.common.config.SmsConfig
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import java.util.*


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
    ]
)
class IdleServerApplication {

    fun main(args: Array<String>) {
        runApplication<IdleServerApplication>(*args)
    }

    @PostConstruct
    fun started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
    }

}


