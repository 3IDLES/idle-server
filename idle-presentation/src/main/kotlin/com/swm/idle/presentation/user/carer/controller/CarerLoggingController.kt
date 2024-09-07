package com.swm.idle.presentation.user.carer.controller

import com.swm.idle.application.user.carer.facade.CarerFacadeService
import com.swm.idle.presentation.user.carer.api.CarerLoggingApi
import com.swm.idle.support.transfer.user.common.logging.GetMyCarerInfoResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class CarerLoggingController(
    private val carerFacadeService: CarerFacadeService,
) : CarerLoggingApi {

    override fun getMyCarerInfo(): GetMyCarerInfoResponse {
        return carerFacadeService.getMyCarerInfo()
    }

}
