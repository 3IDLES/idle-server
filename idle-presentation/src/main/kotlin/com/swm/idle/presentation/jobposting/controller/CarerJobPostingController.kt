package com.swm.idle.presentation.jobposting.controller

import com.swm.idle.application.jobposting.service.facade.CarerJobPostingFacadeService
import com.swm.idle.presentation.jobposting.api.CarerJobPostingApi
import com.swm.idle.support.transfer.jobposting.carer.CarerJobPostingResponse
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class CarerJobPostingController(
    private val carerJobPostingFacadeService: CarerJobPostingFacadeService,
) : CarerJobPostingApi {

    override fun getJobPosting(jobPostingId: UUID): CarerJobPostingResponse {
        return carerJobPostingFacadeService.getJobPosting(jobPostingId)
    }

}
