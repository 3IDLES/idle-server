package com.swm.idle.infrastructure.sms.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("sms")
data class SmsProperties(
    val accessKey: String,
    val secretKey: String,
    val sendingNumber: String,
)
