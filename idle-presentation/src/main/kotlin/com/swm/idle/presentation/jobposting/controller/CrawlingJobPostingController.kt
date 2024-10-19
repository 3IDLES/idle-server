package com.swm.idle.presentation.jobposting.controller

import com.swm.idle.application.common.converter.PointConverter
import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.jobposting.facade.CrawlingJobPostingFacadeService
import com.swm.idle.application.user.carer.domain.CarerService
import com.swm.idle.presentation.jobposting.api.CrawlingJobPostingApi
import com.swm.idle.support.transfer.common.CursorScrollRequest
import com.swm.idle.support.transfer.jobposting.carer.CrawlingJobPostingFavoriteResponse
import com.swm.idle.support.transfer.jobposting.carer.CrawlingJobPostingScrollResponse
import com.swm.idle.support.transfer.jobposting.common.CrawlingJobPostingResponse
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class CrawlingJobPostingController(
    private val crawlingJobPostingFacadeService: CrawlingJobPostingFacadeService,
    private val carerService: CarerService,
) : CrawlingJobPostingApi {

    override fun getCrawlingJobPostingDetail(crawlingJobPostingId: UUID): CrawlingJobPostingResponse {
        return crawlingJobPostingFacadeService.getCrawlingJobPosting(crawlingJobPostingId)
    }

    override fun getCrawlingJobPostings(request: CursorScrollRequest): CrawlingJobPostingScrollResponse {
        val carer = carerService.getById(getUserAuthentication().userId)

        val location = PointConverter.convertToPoint(
            latitude = carer.latitude.toDouble(),
            longitude = carer.longitude.toDouble(),
        )

        return crawlingJobPostingFacadeService.getCrawlingJobPostingsInRange(
            request = request,
            location = location
        )
    }

    override fun getFavoriteCrawlingJobPostings(): CrawlingJobPostingFavoriteResponse {
        val carer = carerService.getById(getUserAuthentication().userId)

        val location = PointConverter.convertToPoint(
            latitude = carer.latitude.toDouble(),
            longitude = carer.longitude.toDouble(),
        )

        return crawlingJobPostingFacadeService.getFavoriteCrawlingJobPostings(
            carer = carer,
            location = location,
        )
    }

}
