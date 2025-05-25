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

    /**
     * Retrieves detailed information about a specific job posting.
     *
     * @param jobPostingId The unique identifier of the job posting.
     * @return The detailed response for the specified job posting.
     */
    override fun getJobPosting(jobPostingId: UUID): CarerJobPostingResponse {
        return carerJobPostingFacadeService.getJobPostingDetail(jobPostingId)
    }

    /**
     * Retrieves a paginated list of carer job postings based on the provided cursor scroll request.
     *
     * @param request Cursor-based pagination parameters for fetching job postings.
     * @return A scrollable response containing carer job postings within the specified range.
     */
    override fun getJobPostings(request: CursorScrollRequest): CarerJobPostingScrollResponse {
        return carerJobPostingFacadeService.getJobPostingsInRange(request)
    }

    /**
     * Retrieves a paginated list of job postings to which the current user has applied.
     *
     * @param request Cursor-based pagination parameters for scrolling through applied job postings.
     * @return A scrollable response containing the user's applied job postings.
     */
    override fun getAppliedJobPostings(
        request: CursorScrollRequest,
    ): CarerAppliedJobPostingScrollResponse {
        return carerJobPostingFacadeService.getAppliedJobPostings(request)
    }

    /**
     * Marks a job posting as a favorite for the currently authenticated carer.
     *
     * Associates the specified job posting with the current user's favorites using the provided job posting type.
     *
     * @param jobPostingId The unique identifier of the job posting to be favorited.
     * @param request Contains the job posting type for the favorite action.
     */
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

    /**
     * Removes the specified job posting from the current user's list of favorites.
     *
     * @param jobPostingId The unique identifier of the job posting to unfavorite.
     */
    override fun deleteJobPostingFavorite(jobPostingId: UUID) {
        jobPostingFavoriteFacadeService.deleteJobPostingFavorite(
            carerId = getUserAuthentication().userId,
            jobPostingId = jobPostingId,
        )
    }

    /**
     * Retrieves the list of job postings favorited by the current user.
     *
     * @return The user's favorite job postings.
     */
    override fun getMyFavoriteJobPostings(): JobPostingFavoriteResponse {
        return carerJobPostingFacadeService.getMyFavoriteJobPostings()
    }

}
