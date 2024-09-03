package com.swm.idle.application.jobposting.facade

import com.swm.idle.application.common.converter.PointConverter
import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.jobposting.domain.CrawlingJobPostingService
import com.swm.idle.application.jobposting.domain.JobPostingFavoriteService
import com.swm.idle.application.jobposting.domain.JobPostingService
import com.swm.idle.application.user.carer.domain.CarerService
import com.swm.idle.domain.common.dto.JobPostingPreviewDto
import com.swm.idle.infrastructure.client.geocode.service.GeoCodeService
import com.swm.idle.support.transfer.jobposting.carer.CrawlingJobPostingScrollResponse
import com.swm.idle.support.transfer.jobposting.carer.CursorScrollRequest
import com.swm.idle.support.transfer.jobposting.common.CrawlingJobPostingResponse
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Service
import java.util.*

@Service
class CrawlingJobPostingFacadeService(
    private val crawlingJobPostingService: CrawlingJobPostingService,
    private val geoCodeService: GeoCodeService,
    private val carerService: CarerService,
    private val jobPostingService: JobPostingService,
    private val jobPostingFavoriteService: JobPostingFavoriteService,
) {

    fun getCrawlingJobPosting(crawlingJobPostingId: UUID): CrawlingJobPostingResponse {
        val jobPosting = crawlingJobPostingService.getById(crawlingJobPostingId)

        val clientLocationInfo = geoCodeService.search(jobPosting.clientAddress)

        return crawlingJobPostingService.getById(crawlingJobPostingId).let {
            val isFavorite = jobPostingFavoriteService.existsByJobPostingId(crawlingJobPostingId)

            CrawlingJobPostingResponse.from(
                crawlingJobPosting = it,
                longitude = clientLocationInfo.addresses[0].x,
                latitude = clientLocationInfo.addresses[0].y,
                isFavorite = isFavorite,
            )
        }
    }

}
