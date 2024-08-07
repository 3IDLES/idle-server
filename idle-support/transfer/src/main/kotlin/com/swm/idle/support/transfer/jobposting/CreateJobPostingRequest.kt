package com.swm.idle.support.transfer.jobposting

import com.swm.idle.domain.jobposting.vo.ApplyDeadlineType
import com.swm.idle.domain.jobposting.vo.ApplyMethodType
import com.swm.idle.domain.jobposting.vo.LifeAssistanceType
import com.swm.idle.domain.jobposting.vo.MentalStatus
import com.swm.idle.domain.jobposting.vo.PayType
import com.swm.idle.domain.jobposting.vo.Weekdays
import com.swm.idle.domain.user.common.enum.GenderType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Create Job Posting Request",
    description = "공고 등록 요청"
)
data class CreateJobPostingRequest(
    @Schema(description = "근무 요일", example = "[MONDAY, TUESDAY]")
    val weekdays: List<Weekdays>,
    @Schema(description = "근무 시작 시간", example = "09:00")
    val startTime: String,
    @Schema(description = "근무 종료 시간", example = "15:00")
    val endTime: String,
    @Schema(description = "급여 종류", example = "HOURLY")
    val payType: PayType,
    @Schema(description = "급여액")
    val payAmount: Int,
    @Schema(description = "도로명 주소")
    val roadNameAddress: String,
    @Schema(description = "지번 주소")
    val lotNameAddress: String,
    @Schema(description = "고객 성함")
    val clientName: String,
    @Schema(description = "고객 성별")
    val gender: GenderType,
    @Schema(description = "출생년도")
    val birthYear: Int,
    @Schema(description = "몸무게")
    val weight: Int,
    @Schema(description = "요양 등급")
    val careLevel: Int,
    @Schema(description = "인지 상태")
    val mentalStatus: MentalStatus,
    @Schema(description = "질병")
    val disease: String,
    @Schema(description = "식사 보조 여부", name = "isMealAssistance")
    val isMealAssistance: Boolean,
    @Schema(description = "배변 보조 여부", name = "isBowelAssistance")
    val isBowelAssistance: Boolean,
    @Schema(description = "산책 보조 여부", name = "isWalkingAssistance")
    val isWalkingAssistance: Boolean,
    @Schema(description = "일상 보조")
    val lifeAssistance: List<LifeAssistanceType>?,
    @Schema(description = "특이사항")
    val speciality: String,
    @Schema(description = "경력자 우대 여부", name = "isExperiencePreferred")
    val isExperiencePreferred: Boolean,
    @Schema(description = "접수 방법", example = "[CALLING, MESSAGE]")
    val applyMethod: List<ApplyMethodType>,
    @Schema(description = "접수 마감 일자")
    val applyDeadline: String,
    @Schema(description = "접수 마감일 상태")
    val applyDeadlineType: ApplyDeadlineType,
)
