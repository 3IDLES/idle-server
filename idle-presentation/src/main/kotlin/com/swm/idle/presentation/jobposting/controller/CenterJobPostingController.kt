package com.swm.idle.presentation.jobposting.controller

import com.swm.idle.application.jobposting.service.facade.CenterJobPostingFacadeService
import com.swm.idle.presentation.jobposting.api.CenterJobPostingApi
import com.swm.idle.support.transfer.jobposting.center.CenterJobPostingInProgressResponse
import com.swm.idle.support.transfer.jobposting.center.CenterJobPostingResponse
import com.swm.idle.support.transfer.jobposting.center.CreateJobPostingRequest
import com.swm.idle.support.transfer.jobposting.center.UpdateJobPostingRequest
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class CenterJobPostingController(
    private val centerJobPostingFacadeService: CenterJobPostingFacadeService,
) : CenterJobPostingApi {

    override suspend fun createJobPosting(request: CreateJobPostingRequest) {
        centerJobPostingFacadeService.create(request = request)
    }

    override fun updateJobPosting(
        jobPostingId: UUID,
        request: UpdateJobPostingRequest,
    ) {
        centerJobPostingFacadeService.update(
            jobPostingId = jobPostingId,
            request = request
        )
    }

    override fun deleteJobPosting(jobPostingId: UUID) {
        centerJobPostingFacadeService.delete(jobPostingId)
    }

    override fun completeJobPosting(jobPostingId: UUID) {
        centerJobPostingFacadeService.updateToComplete(jobPostingId)
    }

    override fun getJobPosting(jobPostingId: UUID): CenterJobPostingResponse {
        return centerJobPostingFacadeService.getById(jobPostingId)
    }

    override fun getJobPostingInProgress(): CenterJobPostingInProgressResponse {
        return CenterJobPostingInProgressResponse.from(
            centerJobPostingFacadeService.findAllById()
        )
    }

}
