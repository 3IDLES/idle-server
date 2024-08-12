package com.swm.idle.presentation.jobposting.controller

import com.swm.idle.application.jobposting.service.facade.JobPostingFacadeService
import com.swm.idle.presentation.jobposting.api.CenterJobPostingApi
import com.swm.idle.support.transfer.jobposting.CenterJobPostingResponse
import com.swm.idle.support.transfer.jobposting.CreateJobPostingRequest
import com.swm.idle.support.transfer.jobposting.UpdateJobPostingRequest
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class CenterJobPostingController(
    private val jobPostingFacadeService: JobPostingFacadeService,
) : CenterJobPostingApi {

    override suspend fun createJobPosting(request: CreateJobPostingRequest) {
        jobPostingFacadeService.create(request = request)
    }

    override fun updateJobPosting(
        jobPostingId: UUID,
        request: UpdateJobPostingRequest,
    ) {
        jobPostingFacadeService.update(
            jobPostingId = jobPostingId,
            request = request
        )
    }

    override fun deleteJobPosting(jobPostingId: UUID) {
        jobPostingFacadeService.delete(jobPostingId)
    }

    override fun completeJobPosting(jobPostingId: UUID) {
        jobPostingFacadeService.updateToComplete(jobPostingId)
    }

    override fun getJobPosting(jobPostingId: UUID): CenterJobPostingResponse {
        return jobPostingFacadeService.getById(jobPostingId)
    }

}
