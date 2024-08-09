package com.swm.idle.presentation.jobposting.api

import com.swm.idle.presentation.common.security.annotation.Secured
import com.swm.idle.support.transfer.jobposting.CreateJobPostingRequest
import com.swm.idle.support.transfer.jobposting.UpdateJobPostingRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@Tag(name = "Job Posting", description = "구인 공고 API")
@RequestMapping("/api/v1/job-postings", produces = ["application/json;charset=utf-8"])
interface JobPostingApi {

    @Secured
    @Operation(summary = "구인 공고 등록 API")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createJobPosting(
        @RequestBody request: CreateJobPostingRequest,
    )

    @Secured
    @Operation(summary = "구인 공고 수정 API")
    @PatchMapping("/{job-posting-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateJobPosting(
        @PathVariable("job-posting-id") jobPostingId: UUID,
        @RequestBody request: UpdateJobPostingRequest,
    )

}