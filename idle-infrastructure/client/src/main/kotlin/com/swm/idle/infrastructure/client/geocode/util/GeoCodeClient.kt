package com.swm.idle.infrastructure.client.geocode.util

import com.swm.idle.infrastructure.client.geocode.dto.GeoCodeSearchResultResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import java.net.URI

@FeignClient(
    name = "Geo-Code-Client",
    url = "geo-code-search-url"
)
fun interface GeoCodeClient {

    @GetMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun send(
        uri: URI,
        @RequestHeader("X-NCP-APIGW-API-KEY-ID") clientId: String,
        @RequestHeader("X-NCP-APIGW-API-KEY") clientSecret: String,
    ): GeoCodeSearchResultResponse

}
