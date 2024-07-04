package com.swm.idle.infrastructure.sms.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("sms.api")
data class SmsApiProperties(
    val accessKey: String,
    val secretKey: String,
    val sendingNumber: String,
)
