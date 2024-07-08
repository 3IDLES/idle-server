package com.swm.idle.infrastructure.client.center.service

import com.swm.idle.domain.center.vo.BusinessRegistrationNumber
import com.swm.idle.infrastructure.client.center.dto.CompanyValidationClientResponse
import com.swm.idle.infrastructure.client.center.util.CompanyValidationClient
import com.swm.idle.infrastructure.client.common.exception.ClientException
import com.swm.idle.infrastructure.client.common.properties.ClientProperties
import org.springframework.stereotype.Service
import java.net.URI

@Service
class CenterAuthClientService(
    val clientProperties: ClientProperties,
    val companyValidationClient: CompanyValidationClient,
) {

    fun sendCompanyValidationRequest(businessRegistrationNumber: BusinessRegistrationNumber): CompanyValidationClientResponse {
        val uri = generateRequestUri(businessRegistrationNumber)

        val result = runCatching {
            companyValidationClient.findCompany(URI(uri))
        }.getOrElse {
            throw ClientException.ExternalApiException()
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
