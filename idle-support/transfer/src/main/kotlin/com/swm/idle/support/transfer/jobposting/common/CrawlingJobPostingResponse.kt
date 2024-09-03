package com.swm.idle.support.transfer.jobposting.common

import com.fasterxml.jackson.annotation.JsonProperty
import com.swm.idle.domain.jobposting.entity.jpa.CrawledJobPosting
import com.swm.idle.domain.jobposting.enums.JobPostingType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.*

@Schema(
    name = "CrawlingJobPostingResponse",
    description = "크롤링 공고 상세 조회 API",
)
data class CrawlingJobPostingResponse(
    val id: UUID,

    @Schema(description = "모집 요강")
    val content: String,

    @Schema(description = "고객 주소")
    val clientAddress: String,

    @Schema(description = "경도")
    val longitude: String,

    @Schema(description = "위도")
    val latitude: String,

    @Schema(description = "생성 시각")
    val createdAt: LocalDateTime,

    @Schema(description = "급여 정보")
    val payInfo: String,

    @Schema(description = "근무 시간")
    val workingTime: String,

    @Schema(description = "근무 스케줄")
    val workingSchedule: String,

    @Schema(description = "마감 일자")
    val applyDeadline: String,

    @Schema(description = "채용 절차")
    val recruitmentProcess: String,

    @Schema(description = "지원 방법")
    val applyMethod: String,

    @Schema(description = "필요 서류")
    val requiredDocumentation: String,

    @Schema(description = "센터명")
    val centerName: String,

    @Schema(description = "센터 주소")
    val centerAddress: String,

    @Schema(description = "외부 공고 url")
    val jobPostingUrl: String,

    @Schema(description = "공고 타입(워크넷, 케어밋)")
    val jobPostingType: JobPostingType,

    @get:JsonProperty("isFavorite")
    @param:JsonProperty("isFavorite")
    @Schema(description = "즐겨찾기 설정 여부")
    val isFavorite: Boolean,
) {

    companion object {

        fun from(
            crawlingJobPosting: CrawledJobPosting,
            longitude: String,
            latitude: String,
            isFavorite: Boolean,
        ): CrawlingJobPostingResponse {
            return CrawlingJobPostingResponse(
                id = crawlingJobPosting.id,
                content = crawlingJobPosting.content,
                clientAddress = crawlingJobPosting.clientAddress,
                longitude = longitude,
                latitude = latitude,
                createdAt = crawlingJobPosting.createdAt,
                payInfo = crawlingJobPosting.payInfo,
                workingTime = crawlingJobPosting.workTime,
                workingSchedule = crawlingJobPosting.workSchedule,
                applyDeadline = crawlingJobPosting.applyDeadline,
                recruitmentProcess = crawlingJobPosting.recruitmentProcess,
                applyMethod = crawlingJobPosting.applyMethod,
                requiredDocumentation = crawlingJobPosting.requiredDocument,
                centerName = crawlingJobPosting.centerName,
                centerAddress = crawlingJobPosting.centerAddress,
                jobPostingUrl = crawlingJobPosting.directUrl,
                jobPostingType = JobPostingType.WORKNET,
                isFavorite = isFavorite,
            )
        }

    }
}
