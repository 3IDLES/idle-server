package com.swm.idle.support.transfer.jobposting.carer

import com.fasterxml.jackson.annotation.JsonProperty
import com.swm.idle.domain.common.dto.JobPostingPreviewDto
import com.swm.idle.domain.jobposting.enums.ApplyDeadlineType
import com.swm.idle.domain.jobposting.enums.JobPostingType
import com.swm.idle.domain.jobposting.enums.PayType
import com.swm.idle.domain.jobposting.enums.Weekdays
import com.swm.idle.domain.user.common.enum.GenderType
import com.swm.idle.domain.user.common.vo.BirthYear
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Schema(
    name = "JobPostingFavoriteResponse",
    description = "즐겨찾기 공고 전체 조회 API"
)
data class JobPostingFavoriteResponse(
    val favoriteJobPostings: List<MyFavoriteJobPostingDto>,
) {

    data class MyFavoriteJobPostingDto(
        @Schema(description = "공고 ID")
        val id: UUID,

        @Schema(description = "근무 요일", example = "[\"MONDAY\", \"TUESDAY\"]")
        val weekdays: List<Weekdays>,

        @Schema(description = "근무 시작 시간", example = "09:00")
        val startTime: String,

        @Schema(description = "근무 종료 시간", example = "18:00")
        val endTime: String,

        @Schema(description = "급여 유형", example = "HOURLY")
        val payType: PayType,

        @Schema(description = "급여 금액", example = "10000")
        val payAmount: Int,

        @Schema(description = "도로명 주소")
        val roadNameAddress: String,

        @Schema(description = "지번 주소")
        val lotNumberAddress: String,

        @Schema(description = "고객 성별", example = "MAN")
        val gender: GenderType,

        @Schema(description = "나이", example = "65")
        val age: Int,

        @Schema(description = "장기 요양 등급", example = "3")
        val careLevel: Int,

        @get:JsonProperty("isExperiencePreferred")
        @param:JsonProperty("isExperiencePreferred")
        @Schema(description = "경력자 우대 여부", example = "true")
        val isExperiencePreferred: Boolean,

        @Schema(description = "지원 마감 유형", example = "LIMITED")
        val applyDeadlineType: ApplyDeadlineType,

        @Schema(description = "지원 마감일", example = "2024-07-30")
        val applyDeadline: LocalDate?,

        @Schema(description = "직선 거리", example = "760(단위 : 미터)")
        val distance: Int,

        @Schema(description = "지원 시각")
        val applyTime: LocalDateTime?,

        @get:JsonProperty("isFavorite")
        @param:JsonProperty("isFavorite")
        @Schema(description = "즐겨찾기 설정 여부")
        val isFavorite: Boolean,

        @Schema(description = "공고 타입")
        val jobPostingType: JobPostingType = JobPostingType.CAREMEET,

        @Schema(description = "공고 생성 시각")
        val createdAt: LocalDateTime,
    ) {

        companion object {

            fun of(
                jobPostingPreviewDto: JobPostingPreviewDto,
                distance: Int,
            ): MyFavoriteJobPostingDto {
                return MyFavoriteJobPostingDto(
                    id = jobPostingPreviewDto.jobPosting.id,
                    weekdays = jobPostingPreviewDto.jobPostingWeekdays.map { it.weekday },
                    startTime = jobPostingPreviewDto.jobPosting.startTime,
                    endTime = jobPostingPreviewDto.jobPosting.endTime,
                    payType = jobPostingPreviewDto.jobPosting.payType,
                    payAmount = jobPostingPreviewDto.jobPosting.payAmount,
                    roadNameAddress = jobPostingPreviewDto.jobPosting.roadNameAddress,
                    lotNumberAddress = jobPostingPreviewDto.jobPosting.lotNumberAddress,
                    gender = jobPostingPreviewDto.jobPosting.gender,
                    age = BirthYear.calculateAge(jobPostingPreviewDto.jobPosting.birthYear),
                    careLevel = jobPostingPreviewDto.jobPosting.careLevel,
                    isExperiencePreferred = jobPostingPreviewDto.jobPosting.isExperiencePreferred,
                    applyDeadline = jobPostingPreviewDto.jobPosting.applyDeadline,
                    applyDeadlineType = jobPostingPreviewDto.jobPosting.applyDeadlineType,
                    distance = distance,
                    applyTime = jobPostingPreviewDto.applyTime,
                    isFavorite = jobPostingPreviewDto.isFavorite,
                    createdAt = jobPostingPreviewDto.jobPosting.createdAt!!
                )
            }

        }
    }

    companion object {

        fun from(
            favoriteJobPostings: List<MyFavoriteJobPostingDto>,
        ): JobPostingFavoriteResponse {
            return JobPostingFavoriteResponse(favoriteJobPostings)
        }

    }

}

