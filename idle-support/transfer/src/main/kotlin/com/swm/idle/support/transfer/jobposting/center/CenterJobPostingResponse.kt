package com.swm.idle.support.transfer.jobposting.center

import com.fasterxml.jackson.annotation.JsonProperty
import com.swm.idle.domain.jobposting.vo.ApplyDeadlineType
import com.swm.idle.domain.jobposting.vo.ApplyMethodType
import com.swm.idle.domain.jobposting.vo.LifeAssistanceType
import com.swm.idle.domain.jobposting.vo.MentalStatus
import com.swm.idle.domain.jobposting.vo.PayType
import com.swm.idle.domain.jobposting.vo.Weekdays
import com.swm.idle.domain.user.common.enum.GenderType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.util.*

@Schema(
    name = "CenterJobPostingResponse",
    description = "센터 공고 상세 조회 API",
)
data class CenterJobPostingResponse(
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

    @Schema(description = "고객 이름")
    val clientName: String,

    @Schema(description = "고객 성별", example = "MAN")
    val gender: GenderType,

    @Schema(description = "나이", example = "65")
    val age: Int,

    @Schema(description = "고객 체중", example = "70", nullable = true)
    val weight: Int?,

    @Schema(description = "장기 요양 등급", example = "3")
    val careLevel: Int,

    @Schema(description = "정신 상태", example = "NORMAL")
    val mentalStatus: MentalStatus,

    @Schema(description = "질병 정보", nullable = true)
    val disease: String?,

    @get:JsonProperty("isMealAssistance")
    @param:JsonProperty("isMealAssistance")
    @Schema(description = "식사 도움 필요 여부", example = "true")
    val isMealAssistance: Boolean,

    @get:JsonProperty("isBowelAssistance")
    @param:JsonProperty("isBowelAssistance")
    @Schema(description = "배변 도움 필요 여부", example = "true")
    val isBowelAssistance: Boolean,

    @get:JsonProperty("isWalkingAssistance")
    @param:JsonProperty("isWalkingAssistance")
    @Schema(description = "보행 도움 필요 여부", example = "true")
    val isWalkingAssistance: Boolean,

    @Schema(description = "생활 도움 항목", nullable = true, example = "[\"CLEANSING\", \"LAUNDRY\"]")
    val lifeAssistance: List<LifeAssistanceType>?,

    @Schema(description = "추가 요구사항", nullable = true)
    val extraRequirement: String?,

    @get:JsonProperty("isExperiencePreferred")
    @param:JsonProperty("isExperiencePreferred")
    @Schema(description = "경력자 우대 여부", example = "true")
    val isExperiencePreferred: Boolean,

    @Schema(description = "지원 방법", example = "[\"CALLING\", \"MESSAGE\"]")
    val applyMethod: List<ApplyMethodType>,

    @Schema(description = "지원 마감 유형", example = "LIMITED")
    val applyDeadlineType: ApplyDeadlineType,

    @Schema(description = "지원 마감일", example = "2024-07-30")
    val applyDeadline: LocalDate?,
)