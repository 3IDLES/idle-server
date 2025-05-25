package com.swm.idle.support.transfer.jobposting.carer

import com.fasterxml.jackson.annotation.JsonProperty
import com.swm.idle.domain.common.dto.CrawlingJobPostingPreviewDto
import com.swm.idle.domain.jobposting.enums.JobPostingType
import com.swm.idle.support.transfer.common.CrawlingScrollResponse
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(
    name = "CrawlingJobPostingScrollResponse",
    description = "외부 구인 공고 전체 조회 API(1km 내 검색)"
)
data class CrawlingJobPostingScrollResponse(
    override val items: List<CrawlingJobPostingDto>,
    override val next: UUID?,
    override val total: Int,
    override val nextDistance: Int,
) : CrawlingScrollResponse<CrawlingJobPostingScrollResponse.CrawlingJobPostingDto, UUID?>(
    items = items,
    next = next,
    total = total,
    nextDistance = nextDistance
) {

    data class CrawlingJobPostingDto(
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

        @Schema(description = "직선 거리 (*km 이내 위치)", example = "1(단위 : 키로미터)")
        val distance: Int,

        @get:JsonProperty("isFavorite")
        @param:JsonProperty("isFavorite")
        @Schema(description = "즐겨찾기 설정 여부")
        val isFavorite: Boolean,

        @Schema(description = "공고 타입", example = "WORKNET")
        val jobPostingType: JobPostingType = JobPostingType.WORKNET,
    ) {

        companion object {

            fun from(
                crawlingJobPostingPreviewDto: CrawlingJobPostingPreviewDto,
            ): CrawlingJobPostingDto {
                return CrawlingJobPostingDto(
                    id = crawlingJobPostingPreviewDto.crawledJobPosting.id,
                    title = crawlingJobPostingPreviewDto.crawledJobPosting.title,
                    workingTime = crawlingJobPostingPreviewDto.crawledJobPosting.workTime,
                    workingSchedule = crawlingJobPostingPreviewDto.crawledJobPosting.workSchedule,
                    payInfo = crawlingJobPostingPreviewDto.crawledJobPosting.payInfo,
                    applyDeadline = crawlingJobPostingPreviewDto.crawledJobPosting.applyDeadline,
                    distance = crawlingJobPostingPreviewDto.distance,
                    isFavorite = crawlingJobPostingPreviewDto.isFavorite,
                )
            }

        }
    }

    companion object {

        /**
         * Creates a `CrawlingJobPostingScrollResponse` from a list of job posting previews and a distance value.
         *
         * Converts each preview DTO to a `CrawlingJobPostingDto`, sets the `next` value to the last item's ID (or null if the list is empty), assigns the total number of items, and sets the next distance in kilometers.
         *
         * @param items List of job posting preview DTOs to include in the response.
         * @param distance Distance in kilometers to the next item for pagination.
         * @return A populated `CrawlingJobPostingScrollResponse` representing the paginated job postings.
         */
        fun from(
            items: List<CrawlingJobPostingPreviewDto>,
            distance: Long
        ): CrawlingJobPostingScrollResponse {
            return CrawlingJobPostingScrollResponse(
                items = items.map(CrawlingJobPostingDto::from),
                next = items.lastOrNull()?.crawledJobPosting?.id,
                total = items.size,
                nextDistance = distance.toInt()
            )
        }

    }

}
