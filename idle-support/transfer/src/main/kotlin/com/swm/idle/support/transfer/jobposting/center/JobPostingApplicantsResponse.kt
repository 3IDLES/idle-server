package com.swm.idle.support.transfer.jobposting.center

import com.swm.idle.domain.jobposting.entity.jpa.JobPosting
import com.swm.idle.domain.jobposting.vo.ApplyDeadlineType
import com.swm.idle.domain.user.carer.entity.jpa.Carer
import com.swm.idle.domain.user.carer.enums.JobSearchStatus
import com.swm.idle.domain.user.common.enum.GenderType
import com.swm.idle.domain.user.common.vo.BirthYear
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.util.*

@Schema(
    name = "JobPostingApplicantsResponse",
    description = "공고 지원자 전체 조회 응답",
)
data class JobPostingApplicantsResponse(
    val jobPostingSummaryDto: JobPostingSummaryDto,
    val jobPostingApplicants: List<JobPostingApplicantDto>?,
) {

    data class JobPostingSummaryDto(
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

            fun from(jobPosting: JobPosting): JobPostingSummaryDto {
                return JobPostingSummaryDto(
                    id = jobPosting.id,
                    roadNameAddress = jobPosting.roadNameAddress,
                    lotNumberAddress = jobPosting.lotNumberAddress,
                    clientName = jobPosting.clientName,
                    gender = jobPosting.gender,
                    age = BirthYear.calculateAge(jobPosting.birthYear),
                    careLevel = jobPosting.careLevel,
                    applyDeadline = jobPosting.applyDeadline,
                    applyDeadlineType = jobPosting.applyDeadlineType,
                    createdAt = LocalDate.from(jobPosting.createdAt),
                )
            }

        }

    }

    data class JobPostingApplicantDto(
        @Schema(description = "요양 보호사 ID")
        val carerId: UUID,

        @Schema(description = "요양 보호사 성명")
        val name: String,

        @Schema(description = "요양 보호사 나이")
        val age: Int,

        @Schema(description = "요양 보호사 성별")
        val gender: GenderType,

        @Schema(description = "연차")
        val experienceYear: Int?,

        @Schema(description = "프로필 이미지 url")
        val profileImageUrl: String?,

        @Schema(description = "현재 구인 여부")
        val jobSearchStatus: JobSearchStatus,
    ) {

        companion object {

            fun from(applicant: Carer): JobPostingApplicantDto {
                return JobPostingApplicantDto(
                    carerId = applicant.id,
                    name = applicant.name,
                    age = BirthYear.calculateAge(applicant.birthYear),
                    gender = applicant.gender,
                    experienceYear = applicant.experienceYear,
                    profileImageUrl = applicant.profileImageUrl,
                    jobSearchStatus = applicant.jobSearchStatus,
                )
            }

        }

    }

    companion object {

        fun of(
            jobPosting: JobPosting,
            applicants: List<Carer>,
        ): JobPostingApplicantsResponse {
            val jobPostingApplicants = applicants.map(JobPostingApplicantDto::from)

            return JobPostingApplicantsResponse(
                jobPostingSummaryDto = JobPostingSummaryDto.from(jobPosting),
                jobPostingApplicants = jobPostingApplicants
            )
        }

    }

}




