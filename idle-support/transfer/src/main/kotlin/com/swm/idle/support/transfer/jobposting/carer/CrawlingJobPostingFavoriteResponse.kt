package com.swm.idle.support.transfer.jobposting.carer

import com.fasterxml.jackson.annotation.JsonProperty
import com.swm.idle.domain.jobposting.entity.jpa.CrawledJobPosting
import com.swm.idle.domain.jobposting.enums.JobPostingType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.*

@Schema(
    name = "CrawlingJobPostingFavoriteResponse",
    description = "즐겨찾기한 크롤링 공고 전체 조회 API"
)
data class CrawlingJobPostingFavoriteResponse(
    val favoriteCrawlingJobPostings: List<CrawlingJobPostingFavoriteDto>,
) {

    data class CrawlingJobPostingFavoriteDto(
        @Schema(description = "공고 ID")
        val id: UUID,

        @Schema(description = "공고 제목")
        val title: String,

        @Schema(description = "근무 시간")
        val workingTime: String,

        @Schema(description = "근무 스케줄")
        val workingSchedule: String,

        @Schema(description = "급여 정보")
        val payInfo: String,

        @Schema(description = "공고 모집 마감 기한")
        val applyDeadline: String,

        @Schema(description = "직선 거리", example = "760(단위 : 미터)")
        val distance: Int,

        @get:JsonProperty("isFavorite")
        @param:JsonProperty("isFavorite")
        @Schema(description = "즐겨찾기 설정 여부, 항상 true")
        val isFavorite: Boolean = true,

        @Schema(description = "공고 타입", example = "WORKNET")
        val jobPostingType: JobPostingType = JobPostingType.WORKNET,

        @Schema(description = "공고 생성 시각")
        val createdAt: LocalDateTime,
    ) {

        companion object {

            fun from(
                crawledJobPosting: CrawledJobPosting,
                distance: Int,
            ): CrawlingJobPostingFavoriteDto {
                return CrawlingJobPostingFavoriteDto(
                    id = crawledJobPosting.id,
                    title = crawledJobPosting.title,
                    workingTime = crawledJobPosting.workTime,
                    workingSchedule = crawledJobPosting.workSchedule,
                    payInfo = crawledJobPosting.payInfo,
                    applyDeadline = crawledJobPosting.applyDeadline.toString(),
                    distance = distance,
                    isFavorite = true,
                    createdAt = crawledJobPosting.createdAt,
                )
            }

        }
    }

    companion object {

        fun from(
            favoriteCrawlingJobPostings: List<CrawlingJobPostingFavoriteDto>,
        ): CrawlingJobPostingFavoriteResponse {
            return CrawlingJobPostingFavoriteResponse(favoriteCrawlingJobPostings)
        }

    }

}
