package com.swm.idle.support.transfer.jobposting.carer

import com.fasterxml.jackson.annotation.JsonProperty
import com.swm.idle.domain.common.dto.JobPostingWithWeekdaysDto
import com.swm.idle.domain.jobposting.vo.ApplyDeadlineType
import com.swm.idle.domain.jobposting.vo.PayType
import com.swm.idle.domain.jobposting.vo.Weekdays
import com.swm.idle.domain.user.common.enum.GenderType
import com.swm.idle.domain.user.common.vo.BirthYear
import com.swm.idle.support.transfer.common.ScrollResponse
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.util.*

@Schema(
    name = "JobPostingsResponse",
    description = "요양 보호사 공고 반경범위 검색 결과 응답",
)
data class CarerJobPostingScrollResponse(
    override val items: List<JobPostingDto>,
    override val next: UUID?,
    override val total: Int,
) : ScrollResponse<CarerJobPostingScrollResponse.JobPostingDto, UUID?>(
    items = items,
    next = next,
    total = total,
) {

    data class JobPostingDto(
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
    ) {

        companion object {

            fun from(
                jobPostingWithWeekdaysDto: JobPostingWithWeekdaysDto,
            ): JobPostingDto {
                return JobPostingDto(
                    id = jobPostingWithWeekdaysDto.jobPosting.id,
                    weekdays = jobPostingWithWeekdaysDto.jobPostingWeekdays.map { it.weekday },
                    startTime = jobPostingWithWeekdaysDto.jobPosting.startTime,
                    endTime = jobPostingWithWeekdaysDto.jobPosting.endTime,
                    payType = jobPostingWithWeekdaysDto.jobPosting.payType,
                    payAmount = jobPostingWithWeekdaysDto.jobPosting.payAmount,
                    roadNameAddress = jobPostingWithWeekdaysDto.jobPosting.roadNameAddress,
                    lotNumberAddress = jobPostingWithWeekdaysDto.jobPosting.lotNumberAddress,
                    gender = jobPostingWithWeekdaysDto.jobPosting.gender,
                    age = BirthYear.calculateAge(jobPostingWithWeekdaysDto.jobPosting.birthYear),
                    careLevel = jobPostingWithWeekdaysDto.jobPosting.careLevel,
                    isExperiencePreferred = jobPostingWithWeekdaysDto.jobPosting.isExperiencePreferred,
                    applyDeadline = jobPostingWithWeekdaysDto.jobPosting.applyDeadline,
                    applyDeadlineType = jobPostingWithWeekdaysDto.jobPosting.applyDeadlineType,
                    distance = jobPostingWithWeekdaysDto.distance,
                )
            }

        }
    }

    companion object {

        fun from(
            items: List<JobPostingWithWeekdaysDto>,
            next: UUID?,
            total: Int,
        ): CarerJobPostingScrollResponse {
            return CarerJobPostingScrollResponse(
                items = items.map(JobPostingDto::from),
                next = next,
                total = total,
            )
        }
    }

}

