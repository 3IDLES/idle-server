package com.swm.idle.application.jobposting.service.domain

import com.swm.idle.application.jobposting.service.vo.JobPostingInfo
import com.swm.idle.domain.jobposting.entity.jpa.JobPosting
import com.swm.idle.domain.jobposting.repository.jpa.JobPostingJpaRepository
import com.swm.idle.support.common.uuid.UuidCreator
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class JobPostingService(
    private val jobPostingJpaRepository: JobPostingJpaRepository,
) {

    // TODO : 지도 geo coding API 통해 좌표값 반환하여 저장 필요.
    fun create(
        centerId: UUID,
        jobPostingInfo: JobPostingInfo,
    ): JobPosting {
        return jobPostingJpaRepository.save(
            JobPosting(
                id = UuidCreator.create(),
                centerId = centerId,
                startTime = jobPostingInfo.startTime,
                endTime = jobPostingInfo.endTime,
                payType = jobPostingInfo.payType,
                payAmount = jobPostingInfo.payAmount,
                roadNameAddress = jobPostingInfo.roadNameAddress,
                lotNameAddress = jobPostingInfo.lotNameAddress,
                longitude = null,
                latitude = null,
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
                speciality = jobPostingInfo.speciality,
                isExperiencePreferred = jobPostingInfo.isExperiencePreferred,
                applyDeadline = LocalDate.parse(
                    jobPostingInfo.applyDeadline,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")
                ),
                applyDeadlineType = jobPostingInfo.applyDeadlineType,
            )
        )
    }

}
