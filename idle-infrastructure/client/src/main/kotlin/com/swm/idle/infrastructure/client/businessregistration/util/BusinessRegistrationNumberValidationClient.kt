package com.swm.idle.infrastructure.client.businessregistration.util

import com.swm.idle.infrastructure.client.businessregistration.dto.BusinessRegistrationNumberValidationResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import java.net.URI

@FeignClient(
    name = "Business-Registration-Number-Validation-Client",
    url = "business-registration-number-validation-url"
)
fun interface BusinessRegistrationNumberValidationClient {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun findByBusinessRegistrationNumber(uri: URI): BusinessRegistrationNumberValidationResponse

}
