package com.swm.idle.api.auth.center.facade

import com.swm.idle.api.auth.center.dto.ValidateBusinessRegistrationNumberResponse
import com.swm.idle.domain.center.service.CenterManagerService
import com.swm.idle.domain.center.vo.BusinessRegistrationNumber
import com.swm.idle.domain.center.vo.Identifier
import com.swm.idle.domain.center.vo.Password
import com.swm.idle.domain.sms.vo.PhoneNumber
import com.swm.idle.infrastructure.client.center.service.CenterAuthClientService
import com.swm.idle.infrastructure.client.common.exception.ClientException
import org.springframework.stereotype.Service

@Service
class CenterAuthFacadeService(
    private val centerManagerService: CenterManagerService,
    private val centerAuthClientService: CenterAuthClientService,
) {
    fun join(
        identifier: Identifier,
        password: Password,
        phoneNumber: PhoneNumber,
        managerName: String,
        centerBusinessRegistrationNumber: BusinessRegistrationNumber,
    ) {
        centerManagerService.save(
            identifier = identifier,
            password = password,
            phoneNumber = phoneNumber,
            managerName = managerName,
            centerBusinessRegistrationNumber = centerBusinessRegistrationNumber,
        )
    }

    fun validateCompany(businessRegistrationNumber: BusinessRegistrationNumber): ValidateBusinessRegistrationNumberResponse {
        val result =
            centerAuthClientService.sendCompanyValidationRequest(businessRegistrationNumber)

        val item = result.items.firstOrNull()
            ?: throw ClientException.CompanyNotFoundException()

        return ValidateBusinessRegistrationNumberResponse(
            businessRegistrationNumber = item.bno,
            companyName = item.company,
        )
    }

}
