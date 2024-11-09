package com.swm.idle.application.jobposting.facade

import com.swm.idle.application.common.converter.PointConverter
import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.jobposting.domain.CrawlingJobPostingService
import com.swm.idle.application.jobposting.domain.JobPostingFavoriteService
import com.swm.idle.application.user.carer.domain.CarerService
import com.swm.idle.domain.common.dto.CrawlingJobPostingPreviewDto
import com.swm.idle.domain.user.carer.entity.jpa.Carer
import com.swm.idle.infrastructure.client.geocode.service.GeoCodeService
import com.swm.idle.support.transfer.common.CursorScrollRequest
import com.swm.idle.support.transfer.jobposting.carer.CrawlingJobPostingFavoriteResponse
import com.swm.idle.support.transfer.jobposting.carer.CrawlingJobPostingScrollResponse
import com.swm.idle.support.transfer.jobposting.common.CrawlingJobPostingResponse
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Service
import java.util.*

@Service
class CrawlingJobPostingFacadeService(
    private val crawlingJobPostingService: CrawlingJobPostingService,
    private val geoCodeService: GeoCodeService,
    private val carerService: CarerService,
    private val jobPostingFavoriteService: JobPostingFavoriteService,
) {

    fun getCrawlingJobPosting(crawlingJobPostingId: UUID): CrawlingJobPostingResponse {
        val carer = getUserAuthentication().userId.let {
            carerService.getById(it)
        }

        val jobPosting = crawlingJobPostingService.getById(crawlingJobPostingId)

        val distance = crawlingJobPostingService.calculateDistance(
            jobPosting,
            PointConverter.convertToPoint(
                latitude = carer.latitude.toDouble(),
                longitude = carer.longitude.toDouble(),
            )
        )

        return crawlingJobPostingService.getById(crawlingJobPostingId).let {
            val isFavorite =
                jobPostingFavoriteService.existsByJobPostingId(crawlingJobPostingId)

            CrawlingJobPostingResponse.from(
                crawlingJobPosting = it,
                longitude = jobPosting.location.x,
                latitude = jobPosting.location.y,
                isFavorite = isFavorite,
                distance = distance,
            )
        }
    }

    fun getCrawlingJobPostingsInRange(
        request: CursorScrollRequest,
        location: Point,
    ): CrawlingJobPostingScrollResponse {
        val (items, next) = scrollByCarerLocationInRange(
            location = location,
            next = request.next,
            limit = request.limit
        )

        return CrawlingJobPostingScrollResponse.from(
            items = items,
            next = next,
            total = items.size,
        )
    }

    fun getFavoriteCrawlingJobPostings(
        carer: Carer,
        location: Point,
    ): CrawlingJobPostingFavoriteResponse {
        val crawlingJobPostings = crawlingJobPostingService.findMyFavoritesByCarerId(carer.id)

        crawlingJobPostings?.map { crawledJobPosting ->
            val distance = crawlingJobPostingService.calculateDistance(
                crawledJobPosting,
                PointConverter.convertToPoint(
                    latitude = carer.latitude.toDouble(),
                    longitude = carer.longitude.toDouble(),
                )
            )

            CrawlingJobPostingFavoriteResponse.CrawlingJobPostingFavoriteDto.from(
                crawledJobPosting = crawledJobPosting,
                distance = distance,
            )
        }.let {
            return CrawlingJobPostingFavoriteResponse.from(it!!)
        }
    }

    private fun scrollByCarerLocationInRange(
        location: Point,
        next: UUID?,
        limit: Long,
    ): Pair<List<CrawlingJobPostingPreviewDto>, UUID?> {
        val carer = getUserAuthentication().userId.let {
            carerService.getById(it)
        }

        val crawlingJobPostingPreviewDtos = crawlingJobPostingService.findAllByCarerLocationInRange(
            carerId = carer.id,
            location = location,
            next = next,
            limit = limit + 1,
        )

        val carerLocation = getUserAuthentication().userId.let {
            carerService.getById(it)
        }.let {
            PointConverter.convertToPoint(
                latitude = it.latitude.toDouble(),
                longitude = it.longitude.toDouble(),
            )
        }

        for (crawlingJobPostingPreviewDto in crawlingJobPostingPreviewDtos) {
            crawlingJobPostingPreviewDto.distance = crawlingJobPostingService.calculateDistance(
                crawlingJobPostingPreviewDto.crawledJobPosting,
                carerLocation
            )
        }

        val newNext =
            if (crawlingJobPostingPreviewDtos.size > limit) crawlingJobPostingPreviewDtos.last().crawledJobPosting.id else null
        val items =
            if (newNext == null) crawlingJobPostingPreviewDtos else crawlingJobPostingPreviewDtos.subList(
                0,
                limit.toInt()
            )
        return items to newNext
    }

}
