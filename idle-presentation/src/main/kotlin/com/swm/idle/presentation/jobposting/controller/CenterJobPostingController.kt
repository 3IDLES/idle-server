package com.swm.idle.presentation.jobposting.controller

import com.swm.idle.application.jobposting.facade.CenterPostingFacadeService
import com.swm.idle.presentation.jobposting.api.CenterJobPostingApi
import com.swm.idle.support.transfer.jobposting.center.CenterJobPostingListResponse
import com.swm.idle.support.transfer.jobposting.center.CenterJobPostingResponse
import com.swm.idle.support.transfer.jobposting.center.CreateJobPostingRequest
import com.swm.idle.support.transfer.jobposting.center.JobPostingApplicantCountResponse
import com.swm.idle.support.transfer.jobposting.center.JobPostingApplicantsResponse
import com.swm.idle.support.transfer.jobposting.center.UpdateJobPostingRequest
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class CenterJobPostingController(
    private val centerJobPostingFacadeService: CenterPostingFacadeService,
) : CenterJobPostingApi {

    /**
     * Creates a new job posting with the provided request data.
     *
     * Initiates the creation process asynchronously by delegating to the facade service.
     */
    override suspend fun createJobPosting(request: CreateJobPostingRequest) {
        centerJobPostingFacadeService.create(request = request)
    }

    /**
     * Updates an existing job posting with new information.
     *
     * @param jobPostingId The unique identifier of the job posting to update.
     * @param request The update request containing new job posting details.
     */
    override fun updateJobPosting(jobPostingId: UUID, request: UpdateJobPostingRequest) {
        centerJobPostingFacadeService.update(jobPostingId, request)
    }

    /**
     * Deletes the job posting identified by the given UUID.
     *
     * @param jobPostingId The unique identifier of the job posting to delete.
     */
    override fun deleteJobPosting(jobPostingId: UUID) {
        centerJobPostingFacadeService.delete(jobPostingId)
    }

    override fun completeJobPosting(jobPostingId: UUID) {
        centerJobPostingFacadeService.updateToCompleted(jobPostingId)
    }

    override fun getJobPostingDetail(jobPostingId: UUID): CenterJobPostingResponse {
        return centerJobPostingFacadeService.getJobPostingDetail(jobPostingId)
    }

    override fun getJobPostingInProgress(): CenterJobPostingListResponse {
        return CenterJobPostingListResponse.from(
            centerJobPostingFacadeService.findAllInProgressById()
        )
    }

    override fun getJobPostingCompleted(): CenterJobPostingListResponse {
        return CenterJobPostingListResponse.from(
            centerJobPostingFacadeService.findAllCompletedById()
        )
    }

    override fun getJobPostingApplicants(jobPostingId: UUID): JobPostingApplicantsResponse {
        return centerJobPostingFacadeService.getJobPostingApplicants(jobPostingId)
    }

    override fun getJobPostingApplicantCount(jobPostingId: UUID): JobPostingApplicantCountResponse {
        return centerJobPostingFacadeService.getApplicantCount(jobPostingId)
    }

}
