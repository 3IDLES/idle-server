package com.swm.idle.application.jobposting.domain

import com.swm.idle.application.common.converter.PointConverter
import com.swm.idle.application.jobposting.vo.JobPostingInfo
import com.swm.idle.domain.common.dto.JobPostingPreviewDto
import com.swm.idle.domain.common.exception.PersistenceException
import com.swm.idle.domain.jobposting.entity.jpa.JobPosting
import com.swm.idle.domain.jobposting.repository.jpa.JobPostingJpaRepository
import com.swm.idle.domain.jobposting.repository.querydsl.JobPostingQueryRepository
import com.swm.idle.domain.jobposting.repository.querydsl.JobPostingSpatialQueryRepository
import com.swm.idle.domain.jobposting.vo.ApplyDeadlineType
import com.swm.idle.domain.jobposting.vo.MentalStatus
import com.swm.idle.domain.jobposting.vo.PayType
import com.swm.idle.domain.user.common.enum.GenderType
import com.swm.idle.domain.user.common.vo.BirthYear
import org.locationtech.jts.geom.Point
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class JobPostingService(
    private val jobPostingJpaRepository: JobPostingJpaRepository,
    private val jobPostingSpatialQueryRepository: JobPostingSpatialQueryRepository,
    private val jobPostingQueryRepository: JobPostingQueryRepository,
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
                applyDeadline = jobPostingInfo.applyDeadline?.let {
                    LocalDate.parse(
                        it,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    )
                } ?: LocalDate.now().plusMonths(1),
                applyDeadlineType = jobPostingInfo.applyDeadlineType,
                location = PointConverter.convertToPoint(
                    jobPostingInfo.latitude.toDouble(),
                    jobPostingInfo.longitude.toDouble(),
                )
            )
        ).also {
            it.active()
        }

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
            location = PointConverter.convertToPoint(
                longitude.toDouble(),
                latitude.toDouble()
            )
        )
    }

    fun delete(jobPosting: JobPosting) {
        jobPosting.delete()
    }

    fun updateToComplete(jobPosting: JobPosting) {
        jobPosting.updateToComplete()
    }

    fun findAllByCarerLocationInRange(
        location: Point,
        next: UUID?,
        limit: Long,
    ): List<JobPostingPreviewDto> {
        return jobPostingSpatialQueryRepository.findAllWithWeekdaysInRange(
            location = location,
            next = next,
            limit = limit,
        )
    }

    fun findAllInProgress(centerId: UUID): List<JobPosting> {
        return jobPostingJpaRepository.findAllInProgress(centerId)
    }

    fun findAllCompleted(centerId: UUID): List<JobPosting> {
        return jobPostingJpaRepository.findAllCompleted(centerId)
    }

    fun findAllByCarerApplyHistory(
        next: UUID?,
        limit: Long,
        carerId: UUID,
    ): List<JobPostingPreviewDto> {
        return jobPostingQueryRepository.findAllByCarerApplyHistory(
            next = next,
            limit = limit,
            carerId = carerId,
        )
    }

    fun calculateDistance(
        jobPosting: JobPosting,
        carerLocation: Point,
    ): Int {
        return jobPostingJpaRepository.calculateDistance(
            jobPosting.location,
            carerLocation
        ).toInt()
    }

}
