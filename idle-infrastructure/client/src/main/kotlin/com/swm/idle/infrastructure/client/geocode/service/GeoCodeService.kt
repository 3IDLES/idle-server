package com.swm.idle.infrastructure.client.geocode.service

import com.swm.idle.infrastructure.client.geocode.dto.GeoCodeSearchResultResponse
import com.swm.idle.infrastructure.client.geocode.exception.GeoCodeException
import com.swm.idle.infrastructure.client.geocode.properties.GeoCodeProperties
import com.swm.idle.infrastructure.client.geocode.util.GeoCodeClient
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Service
class GeoCodeService(
    private val geoCodeClient: GeoCodeClient,
    private val geoCodeProperties: GeoCodeProperties,
) {
    companion object {
        private lateinit var geoCodeClient: GeoCodeClient
        private lateinit var geoCodeProperties: GeoCodeProperties

        fun initialize(client: GeoCodeClient, properties: GeoCodeProperties) {
            geoCodeClient = client
            geoCodeProperties = properties
        }

        fun search(address: String): GeoCodeSearchResultResponse {
            val uri = generateSearchUri(address)
            try {
                return geoCodeClient.send(
                    uri,
                    geoCodeProperties.clientId,
                    geoCodeProperties.clientSecret,
                )
            } catch (e: Exception) {
                throw GeoCodeException.ResultNotFound()
            }
        }

        private fun generateSearchUri(address: String): URI {
            val baseUrl = geoCodeProperties.baseUrl
            val encodedAddress: String = URLEncoder.encode(address, StandardCharsets.UTF_8)
            val uriString = "$baseUrl?query=$encodedAddress"
            return URI.create(uriString)
        }
    }

    @PostConstruct
    fun init() {
        initialize(geoCodeClient, geoCodeProperties)
    }

    fun search(address: String): GeoCodeSearchResultResponse {
        val uri = generateSearchUri(address)
        try {
            return geoCodeClient.send(
                uri,
                geoCodeProperties.clientId,
                geoCodeProperties.clientSecret,
            )
        } catch (e: Exception) {
            throw GeoCodeException.ResultNotFound()
        }

    }

    private fun generateSearchUri(address: String): URI {
        val baseUrl = geoCodeProperties.baseUrl
        val encodedAddress: String = URLEncoder.encode(address, StandardCharsets.UTF_8)
        val uriString = "$baseUrl?query=$encodedAddress"
        return URI.create(uriString)
    }

}
