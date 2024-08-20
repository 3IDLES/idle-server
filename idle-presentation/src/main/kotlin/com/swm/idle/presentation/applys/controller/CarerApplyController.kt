package com.swm.idle.presentation.applys.controller

import com.swm.idle.application.applys.facade.CarerApplyFacadeService
import com.swm.idle.presentation.applys.api.CarerApplyApi
import com.swm.idle.support.transfer.applys.CreateApplyRequest
import org.springframework.web.bind.annotation.RestController

@RestController
class CarerApplyController(
    private val carerApplyFacadeService: CarerApplyFacadeService,
) : CarerApplyApi {

    override fun createApply(
        request: CreateApplyRequest,
    ) {
        carerApplyFacadeService.createApply(
            jobPostingId = request.jobPostingId,
            applyMethodType = request.applyMethodType,
        )
    }

}
