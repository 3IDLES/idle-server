package com.swm.idle.application.jobposting.service.facade

import com.swm.idle.application.jobposting.service.domain.JobPostingApplyMethodService
import com.swm.idle.application.jobposting.service.domain.JobPostingLifeAssistanceService
import com.swm.idle.application.jobposting.service.domain.JobPostingService
import com.swm.idle.application.jobposting.service.domain.JobPostingWeekdayService
import com.swm.idle.application.user.center.service.domain.CenterService
import com.swm.idle.domain.user.common.vo.BirthYear
import com.swm.idle.support.transfer.jobposting.carer.CarerJobPostingResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class CarerJobPostingFacadeService(
    private val jobPostingWeekdayService: JobPostingWeekdayService,
    private val jobPostingLifeAssistanceService: JobPostingLifeAssistanceService,
    private val jobPostingApplyMethodService: JobPostingApplyMethodService,
    private val jobPostingService: JobPostingService,
    private val centerService: CenterService,
) {

    fun getJobPosting(jobPostingId: UUID): CarerJobPostingResponse {
        val weekdays = jobPostingWeekdayService.findByJobPostingId(jobPostingId)?.map { it.weekday }
        val lifeAssistances = jobPostingLifeAssistanceService.findByJobPostingId(jobPostingId)
            ?.map { it.lifeAssistance }
        val applyMethods =
            jobPostingApplyMethodService.findByJobPostingId(jobPostingId)?.map { it.applyMethod }

        jobPostingService.getById(jobPostingId).let {
            val center = centerService.getById(it.centerId)
            return CarerJobPostingResponse(
                id = it.id,
                weekdays = weekdays!!,
                startTime = it.startTime,
                endTime = it.endTime,
                payType = it.payType,
                payAmount = it.payAmount,
                roadNameAddress = it.roadNameAddress,
                lotNumberAddress = it.lotNumberAddress,
                longitude = it.longitude.toString(),
                latitude = it.latitude.toString(),
                gender = it.gender,
                age = BirthYear.calculateAge(it.birthYear),
                weight = it.weight,
                careLevel = it.careLevel,
                mentalStatus = it.mentalStatus,
                disease = it.disease,
                isMealAssistance = it.isMealAssistance,
                isBowelAssistance = it.isBowelAssistance,
                isWalkingAssistance = it.isWalkingAssistance,
                lifeAssistance = lifeAssistances,
                extraRequirement = it.extraRequirement,
                isExperiencePreferred = it.isExperiencePreferred,
                applyMethod = applyMethods!!,
                applyDeadlineType = it.applyDeadlineType,
                applyDeadline = it.applyDeadline,
                centerId = center.id,
                centerName = center.centerName,
                centerRoadNameAddress = center.roadNameAddress,
            )
        }
    }

}
