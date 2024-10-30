package com.swm.idle.application.notification.utils.converter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component

@Component
class NotificationDetailsConverter {

    private val objectMapper: ObjectMapper = ObjectMapper()

    fun convertToMap(details: String): Map<String, Any> {
        return objectMapper.readValue(details)
    }

}
