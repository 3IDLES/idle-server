package com.swm.idle.presentation.auth.carer.controller

import com.swm.idle.application.user.carer.facade.CarerAuthFacadeService
import com.swm.idle.domain.user.common.vo.BirthYear
import com.swm.idle.domain.user.common.vo.PhoneNumber
import com.swm.idle.presentation.auth.carer.api.CarerAuthApi
import com.swm.idle.support.transfer.auth.carer.CarerJoinRequest
import org.springframework.web.bind.annotation.RestController

@RestController
class CarerAuthController(
    private val carerAuthFacadeService: CarerAuthFacadeService,
) : CarerAuthApi {

    override fun join(request: CarerJoinRequest) {
        carerAuthFacadeService.join(
            carerName = request.carerName,
            birthYear = BirthYear(request.birthYear),
            genderType = request.genderType,
            phoneNumber = PhoneNumber(request.phoneNumber),
            roadNameAddress = request.roadNameAddress,
            lotNumberAddress = request.lotNumberAddress,
            longitude = request.longitude,
            latitude = request.latitude,
        )
    }

}