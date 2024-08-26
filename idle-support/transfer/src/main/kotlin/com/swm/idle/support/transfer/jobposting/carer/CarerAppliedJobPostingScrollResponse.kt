package com.swm.idle.support.transfer.jobposting.carer

import com.fasterxml.jackson.annotation.JsonProperty
import com.swm.idle.domain.common.dto.JobPostingWithWeekdaysAndApplyDto
import com.swm.idle.domain.jobposting.vo.ApplyDeadlineType
import com.swm.idle.domain.jobposting.vo.PayType
import com.swm.idle.domain.jobposting.vo.Weekdays
import com.swm.idle.domain.user.common.enum.GenderType
import com.swm.idle.domain.user.common.vo.BirthYear
import com.swm.idle.support.transfer.common.ScrollResponse
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Schema(
    name = "CarerAppliedJobPostingScrollResponse",
    description = "요양 보호사 공고 지원 내역 전체 조회 페이징 응답",
)
data class CarerAppliedJobPostingScrollResponse(
    override val items: List<JobPostingApplyDto>,
    override val next: UUID?,
    override val total: Int,
) : ScrollResponse<CarerAppliedJobPostingScrollResponse.JobPostingApplyDto, UUID?>(
    items = items,
    next = next,
    total = total,
) {

    data class JobPostingApplyDto(
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
        val applyTime: LocalDateTime,

        @get:JsonProperty("isFavorite")
        @param:JsonProperty("isFavorite")
        @Schema(description = "즐겨찾기 설정 여부")
        val isFavorite: Boolean,
    ) {

        companion object {

            fun from(
                jobPostingWithWeekdaysAndApplyDto: JobPostingWithWeekdaysAndApplyDto,
            ): JobPostingApplyDto {
                return JobPostingApplyDto(
                    id = jobPostingWithWeekdaysAndApplyDto.jobPosting.id,
                    weekdays = jobPostingWithWeekdaysAndApplyDto.jobPostingWeekdays.map { it.weekday },
                    startTime = jobPostingWithWeekdaysAndApplyDto.jobPosting.startTime,
                    endTime = jobPostingWithWeekdaysAndApplyDto.jobPosting.endTime,
                    payType = jobPostingWithWeekdaysAndApplyDto.jobPosting.payType,
                    payAmount = jobPostingWithWeekdaysAndApplyDto.jobPosting.payAmount,
                    roadNameAddress = jobPostingWithWeekdaysAndApplyDto.jobPosting.roadNameAddress,
                    lotNumberAddress = jobPostingWithWeekdaysAndApplyDto.jobPosting.lotNumberAddress,
                    gender = jobPostingWithWeekdaysAndApplyDto.jobPosting.gender,
                    age = BirthYear.calculateAge(jobPostingWithWeekdaysAndApplyDto.jobPosting.birthYear),
                    careLevel = jobPostingWithWeekdaysAndApplyDto.jobPosting.careLevel,
                    isExperiencePreferred = jobPostingWithWeekdaysAndApplyDto.jobPosting.isExperiencePreferred,
                    applyDeadline = jobPostingWithWeekdaysAndApplyDto.jobPosting.applyDeadline,
                    applyDeadlineType = jobPostingWithWeekdaysAndApplyDto.jobPosting.applyDeadlineType,
                    distance = jobPostingWithWeekdaysAndApplyDto.distance,
                    applyTime = jobPostingWithWeekdaysAndApplyDto.applyTime,
                    isFavorite = jobPostingWithWeekdaysAndApplyDto.isFavorite
                )
            }

        }
    }

    companion object {

        fun from(
            items: List<JobPostingWithWeekdaysAndApplyDto>,
            next: UUID?,
            total: Int,
        ): CarerAppliedJobPostingScrollResponse {
            return CarerAppliedJobPostingScrollResponse(
                items = items.map(JobPostingApplyDto::from),
                next = next,
                total = total,
            )
        }

    }

}

