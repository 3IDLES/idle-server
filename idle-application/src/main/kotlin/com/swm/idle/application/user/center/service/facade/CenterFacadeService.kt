package com.swm.idle.application.user.center.service.facade

import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.user.center.service.domain.CenterManagerService
import com.swm.idle.application.user.center.service.domain.CenterService
import com.swm.idle.domain.user.center.exception.CenterException
import com.swm.idle.domain.user.center.vo.BusinessRegistrationNumber
import org.springframework.stereotype.Service

@Service
class CenterFacadeService(
    private val centerService: CenterService,
    private val centerManagerService: CenterManagerService,
) {

    fun create(
        officeNumber: String,
        centerName: String,
        roadNameAddress: String,
        lotNumberAddress: String,
        detailedAddress: String,
        longitude: String,
        latitude: String,
        introduce: String?,
    ) {
        val centerManager = getUserAuthentication().userId.let {
            centerManagerService.getById(it)
        }

        centerService.findByBusinessRegistrationNumber(BusinessRegistrationNumber(centerManager.centerBusinessRegistrationNumber))
            ?.let {
                throw CenterException.NotFoundException()
            } ?: also {
            centerService.create(
                officeNumber = officeNumber,
                centerName = centerName,
                businessRegistrationNumber = BusinessRegistrationNumber(centerManager.centerBusinessRegistrationNumber),
                roadNameAddress = roadNameAddress,
                lotNumberAddress = lotNumberAddress,
                detailedAddress = detailedAddress,
                longitude = longitude,
                latitude = latitude,
                introduce = introduce,
            )
        }
    }

}