package com.swm.idle.presentation.jobposting.controller

import com.swm.idle.application.jobposting.facade.CrawlingPostingFacadeService
import com.swm.idle.presentation.jobposting.api.CrawlingJobPostingApi
import com.swm.idle.support.transfer.common.CrawlingCursorScrollRequest
import com.swm.idle.support.transfer.jobposting.carer.CrawlingJobPostingFavoriteResponse
import com.swm.idle.support.transfer.jobposting.carer.CrawlingJobPostingScrollResponse
import com.swm.idle.support.transfer.jobposting.common.CrawlingJobPostingResponse
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class CrawlingJobPostingController(
    private val crawlingPostingFacadeService: CrawlingPostingFacadeService,
) : CrawlingJobPostingApi {

    override fun getCrawlingJobPostingDetail(crawlingJobPostingId: UUID): CrawlingJobPostingResponse {
        return crawlingPostingFacadeService.getCrawlingJobPosting(crawlingJobPostingId)
    }

    override fun getCrawlingJobPostings(request: CrawlingCursorScrollRequest): CrawlingJobPostingScrollResponse {
        return crawlingPostingFacadeService.getCrawlingPostingsInRange(request)
    }

    override fun getFavoriteCrawlingJobPostings(): CrawlingJobPostingFavoriteResponse {
        return crawlingPostingFacadeService.getFavoriteCrawlingJobPostings()
    }

}
