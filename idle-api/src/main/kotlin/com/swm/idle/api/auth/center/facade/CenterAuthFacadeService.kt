package com.swm.idle.api.auth.center.facade

import com.swm.idle.domain.center.service.CenterManagerService
import com.swm.idle.domain.center.vo.BusinessRegistrationNumber
import com.swm.idle.domain.sms.vo.PhoneNumber
import org.springframework.stereotype.Service

@Service
class CenterAuthFacadeService(
    private val centerManagerService: CenterManagerService,
) {
    fun join(
        identifier: String,
        password: String,
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

}
