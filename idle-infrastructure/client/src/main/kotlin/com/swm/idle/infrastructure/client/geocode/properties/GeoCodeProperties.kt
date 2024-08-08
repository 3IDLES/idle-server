package com.swm.idle.infrastructure.client.geocode.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("geocode.naver")
data class GeoCodeProperties(
    val clientId: String,
    val clientSecret: String,
    val baseUrl: String,
)
