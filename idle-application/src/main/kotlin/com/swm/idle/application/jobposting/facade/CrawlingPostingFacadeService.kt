package com.swm.idle.application.jobposting.facade

import com.swm.idle.application.common.converter.PointConverter
import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.jobposting.domain.CrawlingJobPostingService
import com.swm.idle.application.jobposting.domain.JobPostingFavoriteService
import com.swm.idle.application.user.carer.domain.CarerService
import com.swm.idle.domain.common.dto.CrawlingJobPostingPreviewDto
import com.swm.idle.domain.common.enums.EntityStatus
import com.swm.idle.support.transfer.common.CrawlingCursorScrollRequest
import com.swm.idle.support.transfer.jobposting.carer.CrawlingJobPostingFavoriteResponse
import com.swm.idle.support.transfer.jobposting.carer.CrawlingJobPostingScrollResponse
import com.swm.idle.support.transfer.jobposting.common.CrawlingJobPostingResponse
import org.springframework.stereotype.Service
import java.util.*

private const val searchingBreakCount = 3

@Service
class CrawlingPostingFacadeService(
    private val crawlingJobPostingService: CrawlingJobPostingService,
    private val carerService: CarerService,
    private val jobPostingFavoriteService: JobPostingFavoriteService,
) {
    /**
     * Retrieves a paginated list of job postings within a specified distance from the authenticated carer's location.
     *
     * The search expands the distance incrementally until the requested number of postings is found, the maximum distance (30 units) is reached, or three consecutive searches yield no results. Results are accumulated using cursor-based pagination.
     *
     * @param request The scroll request containing pagination and distance parameters.
     * @return A scroll response with the collected job postings and the final search distance used.
     */
    fun getCrawlingPostingsInRange(request: CrawlingCursorScrollRequest): CrawlingJobPostingScrollResponse {
        val carer = carerService.getById(getUserAuthentication().userId)
        val location = PointConverter.convertToPoint(carer)

        var distance = request.distance
        var zeroCount = 0
        var nextCursor: UUID? = request.next

        val result = mutableListOf<CrawlingJobPostingPreviewDto>()
        while (result.size < request.limit && distance <= 30) {
            val currentBatch = crawlingJobPostingService.findAllInRange(
                next = nextCursor,
                location = location,
                distance = distance,
                limit = request.limit  - result.size.toLong()
            )

            if(currentBatch.isEmpty()) zeroCount++
            nextCursor = currentBatch
                .lastOrNull()
                ?.crawledJobPosting
                ?.id

            result.addAll(currentBatch)
            if (result.size >= request.limit
                || zeroCount == searchingBreakCount) break

            distance += 1
        }

        return CrawlingJobPostingScrollResponse.from(result, distance)
    }

    /**
     * Retrieves detailed information about a specific job posting, including its distance from the authenticated carer and favorite status.
     *
     * @param postingId The unique identifier of the job posting to retrieve.
     * @return A response containing the job posting details, the distance from the carer, and whether it is marked as a favorite.
     */
    fun getCrawlingJobPosting(postingId: UUID): CrawlingJobPostingResponse {
        val carer = carerService.getById(getUserAuthentication().userId)
        val posting = crawlingJobPostingService.getById(postingId)
        val distance = crawlingJobPostingService.calculateDistance(posting, PointConverter.convertToPoint(carer))
        val isFavorite = jobPostingFavoriteService.findByByJobPostingId(postingId)?.let {
                            it.entityStatus == EntityStatus.ACTIVE
                        } ?: false

        return CrawlingJobPostingResponse.from(posting, isFavorite, distance)
    }

    /**
     * Retrieves the authenticated carer's favorite job postings with distance information.
     *
     * Returns a response containing a list of the carer's favorite job postings, each including the distance from the carer's current location.
     *
     * @return A response object with favorite job postings and their respective distances.
     */
    fun getFavoriteCrawlingJobPostings(): CrawlingJobPostingFavoriteResponse {
        val carer = carerService.getById(getUserAuthentication().userId)
        val carerPoint = PointConverter.convertToPoint(carer)

        val crawlingJobPostings = crawlingJobPostingService.findMyFavoritesByCarerId(carer.id).orEmpty()

        val dtoList = crawlingJobPostings.map { posting ->
            val distance = crawlingJobPostingService.calculateDistance(posting, carerPoint)
            CrawlingJobPostingFavoriteResponse.CrawlingJobPostingFavoriteDto.from(posting, distance)
        }

        return CrawlingJobPostingFavoriteResponse.from(dtoList)
    }
}
