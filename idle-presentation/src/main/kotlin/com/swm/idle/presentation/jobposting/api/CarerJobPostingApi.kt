package com.swm.idle.presentation.jobposting.api

import com.swm.idle.presentation.common.security.annotation.Secured
import com.swm.idle.support.transfer.jobposting.carer.CarerJobPostingResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@Tag(name = "Job Posting - Carer", description = "요양 보호사 구인 공고 API")
@RequestMapping("/api/v1/job-postings", produces = ["application/json;charset=utf-8"])
interface CarerJobPostingApi {

    @Secured
    @Operation(summary = "요양 보호사 구인 공고 상세 조회 API")
    @GetMapping("/{job-posting-id}/carer")
    @ResponseStatus(HttpStatus.OK)
    fun getJobPosting(@PathVariable(value = "job-posting-id") jobPostingId: UUID): CarerJobPostingResponse

}
