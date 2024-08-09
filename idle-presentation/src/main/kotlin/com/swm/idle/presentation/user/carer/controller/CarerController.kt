package com.swm.idle.presentation.user.carer.controller

import com.swm.idle.application.user.carer.facade.CarerFacadeService
import com.swm.idle.presentation.user.carer.api.CarerApi
import com.swm.idle.support.transfer.user.carer.GetCarerProfileResponse
import com.swm.idle.support.transfer.user.carer.UpdateMyCarerProfileRequest
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class CarerController(
    private val carerFacadeService: CarerFacadeService,
) : CarerApi {

    override fun getMyCarerProfile(): GetCarerProfileResponse {
        return carerFacadeService.getMyCarerProfile()
    }

    override fun getCarerProfile(carerId: UUID): GetCarerProfileResponse {
        return carerFacadeService.getCarerProfile(carerId)
    }

    override fun updateMyCarerProfile(request: UpdateMyCarerProfileRequest) {
        carerFacadeService.updateCarerProfile(
            experienceYear = request.experienceYear,
            roadNameAddress = request.roadNameAddress,
            lotNumberAddress = request.lotNumberAddress,
            introduce = request.introduce,
            speciality = request.speciality,
            jobSearchStatus = request.jobSearchStatus,
        )
    }

}
