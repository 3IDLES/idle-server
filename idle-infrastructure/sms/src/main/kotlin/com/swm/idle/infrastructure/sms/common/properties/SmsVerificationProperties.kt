package com.swm.idle.infrastructure.sms.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("sms.verification")
data class SmsVerificationProperties(
    val expireSeconds: Long,
    val monitoringSeconds: Long,
    val blockingSeconds: Long,
    val maxCount: Int,
)
