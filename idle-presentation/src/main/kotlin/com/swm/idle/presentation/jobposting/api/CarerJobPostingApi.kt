package com.swm.idle.presentation.jobposting.api

import com.swm.idle.presentation.common.security.annotation.Secured
import com.swm.idle.support.transfer.jobposting.carer.CarerAppliedJobPostingScrollResponse
import com.swm.idle.support.transfer.jobposting.carer.CarerJobPostingResponse
import com.swm.idle.support.transfer.jobposting.carer.CarerJobPostingScrollResponse
import com.swm.idle.support.transfer.jobposting.carer.CursorScrollRequest
import com.swm.idle.support.transfer.jobposting.carer.GetMyFavoriteJobPostingScrollResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
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

    @Secured
    @Operation(summary = "요양 보호사 구인 공고 전체 조회 API(3km 내 검색, 최근 등록 순 정렬)")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getJobPostings(
        request: CursorScrollRequest,
    ): CarerJobPostingScrollResponse

    @Secured
    @Operation(summary = "요양 보호사별 지원한 공고 전체 조회 API")
    @GetMapping("/carer/my/applied")
    @ResponseStatus(HttpStatus.OK)
    fun getAppliedJobPostings(
        request: CursorScrollRequest,
    ): CarerAppliedJobPostingScrollResponse

    @Secured
    @Operation(summary = "요양 보호사 공고 즐겨찾기 추가 API")
    @PostMapping("/{job-posting-id}/favorites")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun createJobPostingFavorite(@PathVariable(value = "job-posting-id") jobPostingId: UUID)

    @Secured
    @Operation(summary = "요양 보호사 공고 즐겨찾기 삭제 API")
    @DeleteMapping("/{job-posting-id}/remove-favorites")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteJobPostingFavorite(@PathVariable(value = "job-posting-id") jobPostingId: UUID)

    @Secured
    @Operation(summary = "요양 보호사 즐겨찾기 공고 목록 전체 조회 API")
    @GetMapping("/my/favorites")
    @ResponseStatus(HttpStatus.OK)
    fun getMyFavoriteJobPostings(
        request: CursorScrollRequest,
    ): GetMyFavoriteJobPostingScrollResponse
}
