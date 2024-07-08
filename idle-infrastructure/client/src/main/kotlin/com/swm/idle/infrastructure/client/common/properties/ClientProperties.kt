package com.swm.idle.infrastructure.client.common.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("business-registration-number")
class ClientProperties(
    val apikey: String,
    val path: String,
    val searchType: String,
)
