package com.swm.idle.presentation.user.center.controller

import com.swm.idle.application.user.center.service.facade.CenterFacadeService
import com.swm.idle.domain.user.common.vo.OfficeNumber
import com.swm.idle.presentation.user.center.api.CenterApi
import com.swm.idle.support.transfer.user.center.CreateCenterProfileRequest
import com.swm.idle.support.transfer.user.center.GetCenterProfileResponse
import com.swm.idle.support.transfer.user.center.UpdateCenterProfileRequest
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class CenterController(
    private val centerFacadeService: CenterFacadeService,
) : CenterApi {

    override fun createCenterProfile(request: CreateCenterProfileRequest) {
        centerFacadeService.create(
            officeNumber = OfficeNumber(request.officeNumber),
            centerName = request.centerName,
            roadNameAddress = request.roadNameAddress,
            lotNumberAddress = request.roadNameAddress,
            detailedAddress = request.detailedAddress,
            introduce = request.introduce,
        )
    }

    override fun updateCenterProfile(request: UpdateCenterProfileRequest) {
        centerFacadeService.update(
            officeNumber = request.officeNumber?.let { OfficeNumber(it) },
            introduce = request.introduce,
        )
    }

    override fun getMyCenterProfile(): GetCenterProfileResponse {
        return centerFacadeService.getMyProfile()
    }

    override fun getCenterProfile(centerId: UUID): GetCenterProfileResponse {
        return centerFacadeService.getCenterProfile(centerId)
    }

}
