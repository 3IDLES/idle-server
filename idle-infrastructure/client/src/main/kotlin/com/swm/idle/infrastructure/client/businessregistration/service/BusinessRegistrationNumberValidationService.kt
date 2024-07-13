package com.swm.idle.infrastructure.client.businessregistration.service

import com.swm.idle.domain.user.center.vo.BusinessRegistrationNumber
import com.swm.idle.infrastructure.client.businessregistration.dto.BusinessRegistrationNumberValidationResponse
import com.swm.idle.infrastructure.client.businessregistration.exception.BusinessRegistrationException
import com.swm.idle.infrastructure.client.businessregistration.util.BusinessRegistrationNumberValidationClient
import com.swm.idle.infrastructure.client.common.properties.ClientProperties
import org.springframework.stereotype.Service
import java.net.URI

@Service
class BusinessRegistrationNumberValidationService(
    val clientProperties: ClientProperties,
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
        return clientProperties.path + QUERY_PARAMETER_PREFIX +
                "key=" + clientProperties.apikey + QUERY_PARAMETER_SUFFIX +
                "gb=" + clientProperties.searchType + QUERY_PARAMETER_SUFFIX +
                "q=" + businessRegistrationNumber.value + QUERY_PARAMETER_SUFFIX +
                "type=" + RESPONSE_TYPE
    }

    companion object {
        const val QUERY_PARAMETER_PREFIX = "?"
        const val QUERY_PARAMETER_SUFFIX = "&"
        const val RESPONSE_TYPE = "json"
    }

}
