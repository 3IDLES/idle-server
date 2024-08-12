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

@Schema(
    name = "Update Job Posting Request",
    description = "공고 수정 요청"
)
data class UpdateJobPostingRequest(
    @Schema(description = "근무 요일", example = "[MONDAY, TUESDAY]")
    val weekdays: List<Weekdays>?,
    @Schema(description = "근무 시작 시간", example = "09:00")
    val startTime: String?,
    @Schema(description = "근무 종료 시간", example = "15:00")
    val endTime: String?,
    @Schema(description = "급여 종류", example = "HOURLY")
    val payType: PayType?,
    @Schema(description = "급여액")
    val payAmount: Int?,
    @Schema(description = "도로명 주소")
    val roadNameAddress: String?,
    @Schema(description = "지번 주소")
    val lotNumberAddress: String?,
    @Schema(description = "고객 성함")
    val clientName: String?,
    @Schema(description = "고객 성별")
    val gender: GenderType?,
    @Schema(description = "출생년도")
    val birthYear: Int?,
    @Schema(description = "몸무게")
    val weight: Int?,
    @Schema(description = "요양 등급")
    val careLevel: Int?,
    @Schema(description = "인지 상태")
    val mentalStatus: MentalStatus?,
    @Schema(description = "질병")
    val disease: String?,
    @get:JsonProperty("isMealAssistance")
    @param:JsonProperty("isMealAssistance")
    @Schema(description = "식사 보조 여부", name = "isMealAssistance")
    val isMealAssistance: Boolean?,
    @get:JsonProperty("isBowelAssistance")
    @param:JsonProperty("isBowelAssistance")
    @Schema(description = "배변 보조 여부", name = "isBowelAssistance")
    val isBowelAssistance: Boolean?,
    @get:JsonProperty("isWalkingAssistance")
    @param:JsonProperty("isWalkingAssistance")
    @Schema(description = "산책 보조 여부", name = "isWalkingAssistance")
    val isWalkingAssistance: Boolean?,
    @Schema(description = "일상 보조")
    val lifeAssistance: List<LifeAssistanceType>?,
    @Schema(description = "추가 요청사항")
    val extraRequirement: String?,
    @get:JsonProperty("isExperiencePreferred")
    @param:JsonProperty("isExperiencePreferred")
    @Schema(description = "경력자 우대 여부", name = "isExperiencePreferred")
    val isExperiencePreferred: Boolean?,
    @Schema(description = "접수 방법", example = "[CALLING, MESSAGE]")
    val applyMethod: List<ApplyMethodType>?,
    @Schema(description = "접수 마감 일자")
    val applyDeadline: String?,
    @Schema(description = "접수 마감일 상태")
    val applyDeadlineType: ApplyDeadlineType?,
)
