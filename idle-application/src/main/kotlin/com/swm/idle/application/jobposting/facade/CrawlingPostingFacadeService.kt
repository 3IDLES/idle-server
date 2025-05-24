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

    fun getCrawlingJobPosting(postingId: UUID): CrawlingJobPostingResponse {
        val carer = carerService.getById(getUserAuthentication().userId)
        val posting = crawlingJobPostingService.getById(postingId)
        val distance = crawlingJobPostingService.calculateDistance(posting, PointConverter.convertToPoint(carer))
        val isFavorite = jobPostingFavoriteService.findByByJobPostingId(postingId)?.let {
                            it.entityStatus == EntityStatus.ACTIVE
                        } ?: false

        return CrawlingJobPostingResponse.from(posting, isFavorite, distance)
    }

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
