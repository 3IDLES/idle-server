package com.swm.idle.infrastructure.client.center.util

import com.swm.idle.infrastructure.client.center.dto.CompanyValidationClientResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import java.net.URI

@FeignClient(
    name = "Company-Validation-Client",
    url = "business-registration-number-validation-url"
)
fun interface CompanyValidationClient {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun findCompany(uri: URI): CompanyValidationClientResponse

}
