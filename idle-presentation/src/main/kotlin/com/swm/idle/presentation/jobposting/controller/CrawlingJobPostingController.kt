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
    private val crawlingJobPostingFacadeService: CrawlingPostingFacadeService,
) : CrawlingJobPostingApi {

    /**
     * Retrieves detailed information for a specific crawling job posting.
     *
     * @param crawlingJobPostingId The unique identifier of the crawling job posting.
     * @return The detailed response for the specified crawling job posting.
     */
    override fun getCrawlingJobPostingDetail(crawlingJobPostingId: UUID): CrawlingJobPostingResponse {
        return crawlingJobPostingFacadeService.getCrawlingJobPosting(crawlingJobPostingId)
    }

    /**
     * Retrieves a paginated list of crawling job postings based on the provided cursor scroll request.
     *
     * @param request The cursor-based scroll request specifying pagination and filtering criteria.
     * @return A scroll response containing the list of crawling job postings and pagination information.
     */
    override fun getCrawlingJobPostings(request: CrawlingCursorScrollRequest): CrawlingJobPostingScrollResponse {
        return crawlingJobPostingFacadeService.getCrawlingPostingsInRange(request)
    }

    /**
     * Retrieves the list of favorite crawling job postings.
     *
     * @return A response containing favorite crawling job postings.
     */
    override fun getFavoriteCrawlingJobPostings(): CrawlingJobPostingFavoriteResponse {
        return crawlingJobPostingFacadeService.getFavoriteCrawlingJobPostings()
    }

}
