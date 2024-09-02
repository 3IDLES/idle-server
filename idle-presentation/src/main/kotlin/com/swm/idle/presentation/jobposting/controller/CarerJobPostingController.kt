package com.swm.idle.presentation.jobposting.controller

import com.swm.idle.application.common.converter.PointConverter
import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.jobposting.facade.CarerJobPostingFacadeService
import com.swm.idle.application.jobposting.facade.JobPostingFavoriteFacadeService
import com.swm.idle.application.user.carer.domain.CarerService
import com.swm.idle.presentation.jobposting.api.CarerJobPostingApi
import com.swm.idle.support.transfer.jobposting.carer.CarerAppliedJobPostingScrollResponse
import com.swm.idle.support.transfer.jobposting.carer.CarerJobPostingResponse
import com.swm.idle.support.transfer.jobposting.carer.CarerJobPostingScrollResponse
import com.swm.idle.support.transfer.jobposting.carer.CreateJobPostingFavoriteRequest
import com.swm.idle.support.transfer.jobposting.carer.CursorScrollRequest
import com.swm.idle.support.transfer.jobposting.carer.GetMyFavoriteJobPostingScrollResponse
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class CarerJobPostingController(
    private val carerJobPostingFacadeService: CarerJobPostingFacadeService,
    private val carerService: CarerService,
    private val jobPostingFavoriteFacadeService: JobPostingFavoriteFacadeService,
) : CarerJobPostingApi {

    override fun getJobPosting(jobPostingId: UUID): CarerJobPostingResponse {
        return carerJobPostingFacadeService.getJobPostingDetail(jobPostingId)
    }

    override fun getJobPostings(request: CursorScrollRequest): CarerJobPostingScrollResponse {
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

    override fun getAppliedJobPostings(
        request: CursorScrollRequest,
    ): CarerAppliedJobPostingScrollResponse {
        val carer = carerService.getById(getUserAuthentication().userId)

        return carerJobPostingFacadeService.getAppliedJobPostings(
            request = request,
            carerId = carer.id
        )
    }

    override fun createJobPostingFavorite(
        jobPostingId: UUID,
        request: CreateJobPostingFavoriteRequest,
    ) {
        jobPostingFavoriteFacadeService.createJobPostingFavorite(
            carerId = getUserAuthentication().userId,
            jobPostingId = jobPostingId,
            jobPostingType = request.jobPostingType,
        )
    }

    override fun deleteJobPostingFavorite(jobPostingId: UUID) {
        jobPostingFavoriteFacadeService.deleteJobPostingFavorite(
            carerId = getUserAuthentication().userId,
            jobPostingId = jobPostingId,
        )
    }

    override fun getMyFavoriteJobPostings(
        request: CursorScrollRequest,
    ): GetMyFavoriteJobPostingScrollResponse {
        val carer = carerService.getById(getUserAuthentication().userId)

        return jobPostingFavoriteFacadeService.getMyFavoriteJobPostings(
            request = request,
            carerId = carer.id,
        )
    }

}
