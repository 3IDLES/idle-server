package com.swm.idle.application.jobposting.service.domain

import com.swm.idle.application.jobposting.service.vo.JobPostingInfo
import com.swm.idle.domain.common.exception.PersistenceException
import com.swm.idle.domain.jobposting.entity.jpa.JobPosting
import com.swm.idle.domain.jobposting.repository.jpa.JobPostingJpaRepository
import com.swm.idle.domain.jobposting.vo.ApplyDeadlineType
import com.swm.idle.domain.jobposting.vo.MentalStatus
import com.swm.idle.domain.jobposting.vo.PayType
import com.swm.idle.domain.user.common.enum.GenderType
import com.swm.idle.domain.user.common.vo.BirthYear
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class JobPostingService(
    private val jobPostingJpaRepository: JobPostingJpaRepository,
) {

    fun create(
        centerId: UUID,
        jobPostingInfo: JobPostingInfo,
    ): JobPosting {
        return jobPostingJpaRepository.save(
            JobPosting(
                centerId = centerId,
                startTime = jobPostingInfo.startTime,
                endTime = jobPostingInfo.endTime,
                payType = jobPostingInfo.payType,
                payAmount = jobPostingInfo.payAmount,
                roadNameAddress = jobPostingInfo.roadNameAddress,
                lotNumberAddress = jobPostingInfo.lotNumberAddress,
                longitude = BigDecimal(jobPostingInfo.longitude),
                latitude = BigDecimal(jobPostingInfo.latitude),
                clientName = jobPostingInfo.clientName,
                gender = jobPostingInfo.gender,
                birthYear = jobPostingInfo.birthYear.value,
                weight = jobPostingInfo.weight,
                careLevel = jobPostingInfo.careLevel,
                mentalStatus = jobPostingInfo.mentalStatus,
                disease = jobPostingInfo.disease,
                isMealAssistance = jobPostingInfo.isMealAssistance,
                isBowelAssistance = jobPostingInfo.isBowelAssistance,
                isWalkingAssistance = jobPostingInfo.isWalkingAssistance,
                extraRequirement = jobPostingInfo.extraRequirement,
                isExperiencePreferred = jobPostingInfo.isExperiencePreferred,
                applyDeadline = LocalDate.parse(
                    jobPostingInfo.applyDeadline,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                ),
                applyDeadlineType = jobPostingInfo.applyDeadlineType,
            ).also {
                it.active()
            }
        )
    }

    fun getById(jobPostingId: UUID): JobPosting {
        return jobPostingJpaRepository.findByIdOrNull(jobPostingId)
            ?: throw PersistenceException.ResourceNotFound("구인 공고(id=$jobPostingId)를 찾을 수 없습니다")
    }

    @Transactional
    fun updateWithoutAddress(
        jobPosting: JobPosting,
        startTime: String?,
        endTime: String?,
        payType: PayType?,
        payAmount: Int?,
        clientName: String?,
        gender: GenderType?,
        birthYear: BirthYear?,
        weight: Int?,
        careLevel: Int?,
        mentalStatus: MentalStatus?,
        disease: String?,
        isMealAssistance: Boolean?,
        isBowelAssistance: Boolean?,
        isWalkingAssistance: Boolean?,
        extraRequirement: String?,
        isExperiencePreferred: Boolean?,
        applyDeadline: String?,
        applyDeadlineType: ApplyDeadlineType?,
    ) {
        jobPosting.updateWithoutAddress(
            startTime = startTime,
            endTime = endTime,
            payType = payType,
            payAmount = payAmount,
            clientName = clientName,
            gender = gender,
            birthYear = birthYear?.let { birthYear.value },
            weight = weight,
            careLevel = careLevel,
            mentalStatus = mentalStatus,
            disease = disease,
            isMealAssistance = isMealAssistance,
            isBowelAssistance = isBowelAssistance,
            isWalkingAssistance = isWalkingAssistance,
            extraRequirement = extraRequirement,
            isExperiencePreferred = isExperiencePreferred,
            applyDeadline = applyDeadline?.let {
                LocalDate.parse(
                    it,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                )
            },
            applyDeadlineType = applyDeadlineType,
        )
    }

    @Transactional
    fun update(
        jobPosting: JobPosting,
        startTime: String?,
        endTime: String?,
        payType: PayType?,
        payAmount: Int?,
        roadNameAddress: String,
        lotNumberAddress: String,
        latitude: String,
        longitude: String,
        clientName: String?,
        gender: GenderType?,
        birthYear: BirthYear?,
        weight: Int?,
        careLevel: Int?,
        mentalStatus: MentalStatus?,
        disease: String?,
        isMealAssistance: Boolean?,
        isBowelAssistance: Boolean?,
        isWalkingAssistance: Boolean?,
        extraRequirement: String?,
        isExperiencePreferred: Boolean?,
        applyDeadline: String?,
        applyDeadlineType: ApplyDeadlineType?,
    ) {
        jobPosting.update(
            startTime = startTime,
            endTime = endTime,
            payType = payType,
            payAmount = payAmount,
            roadNameAddress = roadNameAddress,
            lotNumberAddress = lotNumberAddress,
            longitude = BigDecimal(longitude),
            latitude = BigDecimal(latitude),
            clientName = clientName,
            gender = gender,
            birthYear = birthYear?.let { birthYear.value },
            weight = weight,
            careLevel = careLevel,
            mentalStatus = mentalStatus,
            disease = disease,
            isMealAssistance = isMealAssistance,
            isBowelAssistance = isBowelAssistance,
            isWalkingAssistance = isWalkingAssistance,
            extraRequirement = extraRequirement,
            isExperiencePreferred = isExperiencePreferred,
            applyDeadline = applyDeadline?.let {
                LocalDate.parse(
                    it,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                )
            },
            applyDeadlineType = applyDeadlineType,
        )
    }

    fun delete(jobPosting: JobPosting) {
        jobPosting.delete()
    }

    fun updateToComplete(jobPosting: JobPosting) {
        jobPosting.updateToComplete()
    }

}
