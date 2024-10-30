package com.swm.idle.infrastructure.client.businessregistration.service

import com.swm.idle.domain.user.center.vo.BusinessRegistrationNumber
import com.swm.idle.infrastructure.client.businessregistration.dto.BusinessRegistrationNumberValidationResponse
import com.swm.idle.infrastructure.client.businessregistration.exception.BusinessRegistrationException
import com.swm.idle.infrastructure.client.businessregistration.properties.BusinessRegistrationNumberProperties
import com.swm.idle.infrastructure.client.businessregistration.util.BusinessRegistrationNumberValidationClient
import org.springframework.stereotype.Service
import java.net.URI

@Service
class BusinessRegistrationNumberValidationService(
    val businessRegistrationNumberProperties: BusinessRegistrationNumberProperties,
    val businessRegistrationNumberValidationClient: BusinessRegistrationNumberValidationClient,
) {

    fun sendCompanyValidationRequest(businessRegistrationNumber: BusinessRegistrationNumber): BusinessRegistrationNumberValidationResponse {
        val uri = generateRequestUri(businessRegistrationNumber)

        val result = runCatching {
            businessRegistrationNumberValidationClient.findByBusinessRegistrationNumber(URI(uri))
        }.getOrElse {
            throw BusinessRegistrationException.CompanyNotFound()
        }

        return result
    }

    private fun generateRequestUri(businessRegistrationNumber: BusinessRegistrationNumber): String {
        return businessRegistrationNumberProperties.path + QUERY_PARAMETER_PREFIX +
                "key=" + businessRegistrationNumberProperties.apikey + QUERY_PARAMETER_SUFFIX +
                "gb=" + businessRegistrationNumberProperties.searchType + QUERY_PARAMETER_SUFFIX +
                "q=" + businessRegistrationNumber.value + QUERY_PARAMETER_SUFFIX +
                "type=" + RESPONSE_TYPE
    }

    companion object {

        const val QUERY_PARAMETER_PREFIX = "?"
        const val QUERY_PARAMETER_SUFFIX = "&"
        const val RESPONSE_TYPE = "json"
    }

}
