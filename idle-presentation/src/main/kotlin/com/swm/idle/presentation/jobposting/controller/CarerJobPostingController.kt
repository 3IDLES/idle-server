package com.swm.idle.presentation.jobposting.controller

import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.jobposting.facade.CarerPostingFacadeService
import com.swm.idle.application.jobposting.facade.JobPostingFavoriteFacadeService
import com.swm.idle.presentation.jobposting.api.CarerJobPostingApi
import com.swm.idle.support.transfer.common.CursorScrollRequest
import com.swm.idle.support.transfer.jobposting.carer.CarerAppliedJobPostingScrollResponse
import com.swm.idle.support.transfer.jobposting.carer.CarerJobPostingResponse
import com.swm.idle.support.transfer.jobposting.carer.CarerJobPostingScrollResponse
import com.swm.idle.support.transfer.jobposting.carer.CreateJobPostingFavoriteRequest
import com.swm.idle.support.transfer.jobposting.carer.JobPostingFavoriteResponse
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class CarerJobPostingController(
    private val carerJobPostingFacadeService: CarerPostingFacadeService,
    private val jobPostingFavoriteFacadeService: JobPostingFavoriteFacadeService,
) : CarerJobPostingApi {

    override fun getJobPosting(jobPostingId: UUID): CarerJobPostingResponse {
        return carerJobPostingFacadeService.getJobPostingDetail(jobPostingId)
    }

    override fun getJobPostings(request: CursorScrollRequest): CarerJobPostingScrollResponse {
        return carerJobPostingFacadeService.getJobPostingsInRange(request)
    }

    override fun getAppliedJobPostings(
        request: CursorScrollRequest,
    ): CarerAppliedJobPostingScrollResponse {
        return carerJobPostingFacadeService.getAppliedJobPostings(request)
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

    override fun getMyFavoriteJobPostings(): JobPostingFavoriteResponse {
        return carerJobPostingFacadeService.getMyFavoriteJobPostings()
    }

}
