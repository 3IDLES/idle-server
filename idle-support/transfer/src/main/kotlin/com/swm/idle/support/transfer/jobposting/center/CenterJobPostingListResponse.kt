package com.swm.idle.support.transfer.jobposting.center

import com.swm.idle.domain.jobposting.entity.jpa.JobPosting
import com.swm.idle.domain.jobposting.enums.ApplyDeadlineType
import com.swm.idle.domain.user.common.enum.GenderType
import com.swm.idle.domain.user.common.vo.BirthYear
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.util.*

@Schema(
    name = "CenterJobPostingListResponse",
    description = "센터 공고 상세 조회 API",
)
data class CenterJobPostingListResponse(
    val jobPostings: List<CenterJobPostingDto>,
) {

    data class CenterJobPostingDto(
        @Schema(description = "공고 ID")
        val id: UUID,

        @Schema(description = "도로명 주소")
        val roadNameAddress: String,

        @Schema(description = "지번 주소")
        val lotNumberAddress: String,

        @Schema(description = "고객 성명")
        val clientName: String,

        @Schema(description = "성별")
        val gender: GenderType,

        @Schema(description = "나이")
        val age: Int,

        @Schema(description = "요양 등급")
        val careLevel: Int,

        @Schema(description = "지원 마감 유형", example = "LIMITED")
        val applyDeadlineType: ApplyDeadlineType,

        @Schema(description = "지원 마감일", example = "2024-07-30")
        val applyDeadline: LocalDate?,

        @Schema(description = "공고 등록 시각", example = "2024-07-29")
        val createdAt: LocalDate,
    ) {

        companion object {

            fun from(
                jobPosting: JobPosting,
            ): CenterJobPostingDto {
                return CenterJobPostingDto(
                    id = jobPosting.id,
                    roadNameAddress = jobPosting.roadNameAddress,
                    lotNumberAddress = jobPosting.lotNumberAddress,
                    clientName = jobPosting.clientName,
                    gender = jobPosting.gender,
                    age = BirthYear.calculateAge(jobPosting.birthYear),
                    careLevel = jobPosting.careLevel,
                    applyDeadlineType = jobPosting.applyDeadlineType,
                    applyDeadline = jobPosting.applyDeadline,
                    createdAt = jobPosting.createdAt!!.toLocalDate()
                )
            }

        }
    }

    companion object {

        fun from(jobPostings: List<JobPosting>): CenterJobPostingListResponse {
            return CenterJobPostingListResponse(
                jobPostings.map(CenterJobPostingDto::from)
            )
        }
    }

}
