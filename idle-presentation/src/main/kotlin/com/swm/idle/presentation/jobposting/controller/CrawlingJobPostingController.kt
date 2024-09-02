package com.swm.idle.presentation.jobposting.controller

import com.swm.idle.application.jobposting.facade.CrawlingJobPostingFacadeService
import com.swm.idle.presentation.jobposting.api.CrawlingJobPostingApi
import com.swm.idle.support.transfer.jobposting.common.CrawlingJobPostingResponse
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class CrawlingJobPostingController(
    private val crawlingJobPostingFacadeService: CrawlingJobPostingFacadeService,
) : CrawlingJobPostingApi {

    override fun getCrawlingJobPostingDetail(crawlingJobPostingId: UUID): CrawlingJobPostingResponse {
        return crawlingJobPostingFacadeService.getCrawlingJobPosting(crawlingJobPostingId)
    }

}
