package com.swm.idle.support.transfer.jobposting.carer

import com.fasterxml.jackson.annotation.JsonProperty
import com.swm.idle.domain.common.dto.CrawlingJobPostingPreviewDto
import com.swm.idle.domain.jobposting.enums.JobPostingType
import com.swm.idle.support.transfer.common.ScrollResponse
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(
    name = "CrawlingJobPostingScrollResponse",
    description = "외부 구인 공고 전체 조회 API(3km 내 검색)"
)
data class CrawlingJobPostingScrollResponse(
    override val items: List<CrawlingJobPostingDto>,
    override val next: UUID?,
    override val total: Int,
) : ScrollResponse<CrawlingJobPostingScrollResponse.CrawlingJobPostingDto, UUID?>(
    items = items,
    next = next,
    total = total,
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

        @Schema(description = "직선 거리", example = "760(단위 : 미터)")
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
                    applyDeadline = crawlingJobPostingPreviewDto.crawledJobPosting.applyDeadline.toString(),
                    distance = crawlingJobPostingPreviewDto.distance,
                    isFavorite = crawlingJobPostingPreviewDto.isFavorite,
                )
            }

        }
    }

    companion object {

        fun from(
            items: List<CrawlingJobPostingPreviewDto>,
            next: UUID?,
            total: Int,
        ): CrawlingJobPostingScrollResponse {
            return CrawlingJobPostingScrollResponse(
                items = items.map(CrawlingJobPostingDto::from),
                next = next,
                total = total,
            )
        }
    }

}
