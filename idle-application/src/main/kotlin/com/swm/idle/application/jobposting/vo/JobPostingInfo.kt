package com.swm.idle.application.jobposting.vo

import com.swm.idle.domain.jobposting.enums.ApplyDeadlineType
import com.swm.idle.domain.jobposting.enums.MentalStatus
import com.swm.idle.domain.jobposting.enums.PayType
import com.swm.idle.domain.user.common.enum.GenderType
import com.swm.idle.domain.user.common.vo.BirthYear
import com.swm.idle.support.transfer.jobposting.center.CreateJobPostingRequest

data class JobPostingInfo(
    val startTime: String,
    val endTime: String,
    val payType: PayType,
    val payAmount: Int,
    val roadNameAddress: String,
    val lotNumberAddress: String,
    val latitude: String,
    val longitude: String,
    val clientName: String,
    val gender: GenderType,
    val birthYear: BirthYear,
    val weight: Int?,
    val careLevel: Int,
    val mentalStatus: MentalStatus,
    val disease: String?,
    val isMealAssistance: Boolean,
    val isBowelAssistance: Boolean,
    val isWalkingAssistance: Boolean,
    val extraRequirement: String?,
    val isExperiencePreferred: Boolean,
    val applyDeadline: String?,
    val applyDeadlineType: ApplyDeadlineType,
) {

    companion object {

        fun of(
            request: CreateJobPostingRequest,
            latitude: String,
            longitude: String,
        ): JobPostingInfo {
            return JobPostingInfo(
                startTime = request.startTime,
                endTime = request.endTime,
                payType = request.payType,
                payAmount = request.payAmount,
                roadNameAddress = request.roadNameAddress,
                lotNumberAddress = request.lotNumberAddress,
                latitude = latitude,
                longitude = longitude,
                clientName = request.clientName,
                gender = request.gender,
                birthYear = BirthYear(request.birthYear),
                weight = request.weight,
                careLevel = request.careLevel,
                mentalStatus = request.mentalStatus,
                disease = request.disease,
                isMealAssistance = request.isMealAssistance,
                isBowelAssistance = request.isBowelAssistance,
                isWalkingAssistance = request.isWalkingAssistance,
                extraRequirement = request.extraRequirement,
                isExperiencePreferred = request.isExperiencePreferred,
                applyDeadline = request.applyDeadline,
                applyDeadlineType = request.applyDeadlineType,
            )
        }

    }

}
