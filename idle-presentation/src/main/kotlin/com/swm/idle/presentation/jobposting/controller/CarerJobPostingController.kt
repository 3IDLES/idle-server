package com.swm.idle.presentation.jobposting.controller

import com.swm.idle.application.common.converter.PointConverter
import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.jobposting.service.facade.CarerJobPostingFacadeService
import com.swm.idle.application.user.carer.domain.CarerService
import com.swm.idle.presentation.jobposting.api.CarerJobPostingApi
import com.swm.idle.support.transfer.jobposting.carer.CarerJobPostingResponse
import com.swm.idle.support.transfer.jobposting.carer.CarerJobPostingScrollRequest
import com.swm.idle.support.transfer.jobposting.carer.CarerJobPostingsScrollResponse
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class CarerJobPostingController(
    private val carerJobPostingFacadeService: CarerJobPostingFacadeService,
    private val carerService: CarerService,
) : CarerJobPostingApi {

    override fun getJobPosting(jobPostingId: UUID): CarerJobPostingResponse {
        return carerJobPostingFacadeService.getJobPosting(jobPostingId)
    }

    override fun getJobPostingList(request: CarerJobPostingScrollRequest): CarerJobPostingsScrollResponse {
        val carer = carerService.getById(getUserAuthentication().userId)

        val location = PointConverter.convertToPoint(
            latitude = carer.latitude.toDouble(),
            longitude = carer.longitude.toDouble(),
        )

        return carerJobPostingFacadeService.getJobPostingsInRange(
            request = request,
            location = location
        )
    }

}
