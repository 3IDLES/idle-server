package com.swm.idle.presentation.jobposting.api

import com.swm.idle.presentation.common.security.annotation.Secured
import com.swm.idle.support.transfer.jobposting.common.CrawlingJobPostingResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@Tag(name = "Crawling Job Posting", description = "크롤링 공고 API")
@RequestMapping("/api/v1/crwaling-job-postings", produces = ["application/json;charset=utf-8"])
interface CrawlingJobPostingApi {

    @Secured
    @Operation(summary = "크롤링 공고 상세 조회 API")
    @GetMapping("/{crawling-job-posting-id}")
    @ResponseStatus(HttpStatus.OK)
    fun getCrawlingJobPostingDetail(@PathVariable(value = "crawling-job-posting-id") crawlingJobPostingId: UUID): CrawlingJobPostingResponse

}
