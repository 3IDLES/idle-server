package com.swm.idle.presentation.applyment.controller

import com.swm.idle.application.applyment.facade.CarerApplyFacadeService
import com.swm.idle.presentation.applyment.api.CarerApplyApi
import com.swm.idle.support.transfer.applyment.CreateApplyRequest
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
