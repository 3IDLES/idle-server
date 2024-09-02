package com.swm.idle.application.jobposting.facade

import com.swm.idle.application.jobposting.domain.CrawlingJobPostingService
import com.swm.idle.infrastructure.client.geocode.service.GeoCodeService
import com.swm.idle.support.transfer.jobposting.common.CrawlingJobPostingResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class CrawlingJobPostingFacadeService(
    private val crawlingJobPostingService: CrawlingJobPostingService,
    private val geoCodeService: GeoCodeService,
) {

    fun getCrawlingJobPosting(crawlingJobPostingId: UUID): CrawlingJobPostingResponse {
        val jobPosting = crawlingJobPostingService.getById(crawlingJobPostingId)

        val clientLocationInfo = geoCodeService.search(jobPosting.clientAddress)

        return crawlingJobPostingService.getById(crawlingJobPostingId).let {
            CrawlingJobPostingResponse.from(
                crawlingJobPosting = it,
                longitude = clientLocationInfo.addresses[0].x,
                latitude = clientLocationInfo.addresses[0].y,
            )
        }
    }

}
