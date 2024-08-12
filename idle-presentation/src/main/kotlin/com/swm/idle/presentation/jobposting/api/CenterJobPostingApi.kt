package com.swm.idle.presentation.jobposting.api

import com.swm.idle.presentation.common.security.annotation.Secured
import com.swm.idle.support.transfer.jobposting.center.CenterJobPostingResponse
import com.swm.idle.support.transfer.jobposting.center.CreateJobPostingRequest
import com.swm.idle.support.transfer.jobposting.center.UpdateJobPostingRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@Tag(name = "Job Posting - Center", description = "센터 구인 공고 API")
@RequestMapping("/api/v1/job-postings", produces = ["application/json;charset=utf-8"])
interface CenterJobPostingApi {

    @Secured
    @Operation(summary = "구인 공고 등록 API")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createJobPosting(
        @RequestBody request: CreateJobPostingRequest,
    )

    @Secured
    @Operation(summary = "구인 공고 수정 API")
    @PatchMapping("/{job-posting-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateJobPosting(
        @PathVariable(value = "job-posting-id") jobPostingId: UUID,
        @RequestBody request: UpdateJobPostingRequest,
    )

    @Secured
    @Operation(summary = "구인 공고 삭제 API")
    @DeleteMapping("/{job-posting-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteJobPosting(
        @PathVariable(value = "job-posting-id") jobPostingId: UUID,
    )

    @Secured
    @Operation(summary = "구인 공고 채용 종료 API")
    @PatchMapping("/{job-posting-id}/end")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun completeJobPosting(
        @PathVariable(value = "job-posting-id") jobPostingId: UUID,
    )

    @Secured
    @Operation(summary = "센터 구인 공고 상세 조회 API")
    @GetMapping("/{job-posting-id}/center")
    @ResponseStatus(HttpStatus.OK)
    fun getJobPosting(@PathVariable(value = "job-posting-id") jobPostingId: UUID): CenterJobPostingResponse

}
