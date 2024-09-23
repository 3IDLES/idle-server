package com.swm.idle.infrastructure.client.businessregistration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("business-registration-number")
class BusinessRegistrationNumberProperties(
    val apikey: String,
    val path: String,
    val searchType: String,
)
