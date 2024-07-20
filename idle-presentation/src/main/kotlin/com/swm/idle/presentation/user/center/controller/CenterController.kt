package com.swm.idle.presentation.user.center.controller

import com.swm.idle.application.user.center.service.facade.CenterFacadeService
import com.swm.idle.presentation.user.center.api.CenterApi
import com.swm.idle.support.transfer.user.center.CreateCenterProfileRequest
import com.swm.idle.support.transfer.user.center.UpdateCenterProfileRequest
import org.springframework.web.bind.annotation.RestController

@RestController
class CenterController(
    private val centerFacadeService: CenterFacadeService,
) : CenterApi {

    override fun createCenterProfile(request: CreateCenterProfileRequest) {
        centerFacadeService.create(
            officeNumber = request.officeNumber,
            centerName = request.centerName,
            roadNameAddress = request.roadNameAddress,
            lotNumberAddress = request.roadNameAddress,
            detailedAddress = request.detailedAddress,
            longitude = request.longitude,
            latitude = request.latitude,
            introduce = request.introduce,
        )
    }

    override fun updateCenterProfile(request: UpdateCenterProfileRequest) {
        centerFacadeService.update(
            officeNumber = request.officeNumber,
            introduce = request.introduce,
        )
    }

}