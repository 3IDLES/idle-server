package com.swm.idle.application.jobposting.service.facade

import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.jobposting.service.domain.JobPostingApplyMethodService
import com.swm.idle.application.jobposting.service.domain.JobPostingLifeAssistanceService
import com.swm.idle.application.jobposting.service.domain.JobPostingService
import com.swm.idle.application.jobposting.service.domain.JobPostingWeekdayService
import com.swm.idle.application.jobposting.service.vo.JobPostingInfo
import com.swm.idle.domain.user.common.vo.BirthYear
import com.swm.idle.infrastructure.client.geocode.service.GeoCodeService
import com.swm.idle.support.transfer.jobposting.CenterJobPostingResponse
import com.swm.idle.support.transfer.jobposting.CreateJobPostingRequest
import com.swm.idle.support.transfer.jobposting.UpdateJobPostingRequest
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class JobPostingFacadeService(
    private val jobPostingService: JobPostingService,
    private val jobPostingLifeAssistanceService: JobPostingLifeAssistanceService,
    private val jobPostingWeekdayService: JobPostingWeekdayService,
    private val jobPostingApplyMethodService: JobPostingApplyMethodService,
    private val geoCodeService: GeoCodeService,
) {

    @Transactional
    suspend fun create(request: CreateJobPostingRequest) {
        val centerId = getUserAuthentication().userId

        val geoCodeSearchResult = geoCodeService.search(request.roadNameAddress)

        jobPostingService.create(
            centerId = centerId,
            jobPostingInfo = JobPostingInfo.of(
                request = request,
                latitude = geoCodeSearchResult.addresses[0].y,
                longitude = geoCodeSearchResult.addresses[0].x
            )
        ).let { jobPosting ->
            coroutineScope {
                request.lifeAssistance?.let {
                    jobPostingLifeAssistanceService.create(
                        jobPostingId = jobPosting.id,
                        lifeAssistance = request.lifeAssistance!!,
                    )
                }

                launch {
                    jobPostingWeekdayService.create(
                        jobPostingId = jobPosting.id,
                        weekdays = request.weekdays,
                    )
                }

                launch {
                    jobPostingApplyMethodService.create(
                        jobPostingId = jobPosting.id,
                        applyMethods = request.applyMethod,
                    )
                }
            }
        }
    }

    @Transactional
    fun update(
        jobPostingId: UUID,
        request: UpdateJobPostingRequest,
    ) {
        val jobPosting = jobPostingService.getById(jobPostingId)

        val shouldUpdateAddress =
            request.roadNameAddress != null && request.lotNumberAddress != null

        if (shouldUpdateAddress) {
            val geoCodeSearchResult = geoCodeService.search(request.roadNameAddress!!)

            jobPostingService.update(
                jobPosting = jobPosting,
                startTime = request.startTime,
                endTime = request.endTime,
                payType = request.payType,
                payAmount = request.payAmount,
                roadNameAddress = request.roadNameAddress!!,
                lotNumberAddress = request.lotNumberAddress!!,
                latitude = geoCodeSearchResult.addresses[0].y,
                longitude = geoCodeSearchResult.addresses[0].x,
                clientName = request.clientName,
                gender = request.gender,
                birthYear = request.birthYear?.let { BirthYear(it) },
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
        } else {
            jobPostingService.updateWithoutAddress(
                jobPosting = jobPosting,
                startTime = request.startTime,
                endTime = request.endTime,
                payType = request.payType,
                payAmount = request.payAmount,
                clientName = request.clientName,
                gender = request.gender,
                birthYear = request.birthYear?.let { BirthYear(it) },
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

        request.lifeAssistance?.let {
            jobPostingLifeAssistanceService.update(
                jobPosting = jobPosting,
                lifeAssistance = request.lifeAssistance!!,
            )
        }

        request.weekdays?.let {
            jobPostingWeekdayService.update(
                jobPosting = jobPosting,
                weekdays = request.weekdays!!,
            )
        }

        request.applyMethod?.let {
            jobPostingApplyMethodService.update(
                jobPosting = jobPosting,
                applyMethods = request.applyMethod!!,
            )
        }
    }

    @Transactional
    fun delete(jobPostingId: UUID) {
        val jobPosting = jobPostingService.getById(jobPostingId)

        jobPostingService.delete(jobPosting)

        jobPostingLifeAssistanceService.findByJobPostingId(jobPostingId)?.let {
            jobPostingLifeAssistanceService.deleteAll(it)
        }

        jobPostingWeekdayService.findByJobPostingId(jobPostingId)?.let {
            jobPostingWeekdayService.deleteAll(it)
        }

        jobPostingApplyMethodService.findByJobPostingId(jobPostingId)?.let {
            jobPostingApplyMethodService.deleteAll(it)
        }
    }

    @Transactional
    fun updateToComplete(jobPostingId: UUID) {
        jobPostingService.getById(jobPostingId).let {
            jobPostingService.updateToComplete(it)
        }
    }

    fun getById(jobPostingId: UUID): CenterJobPostingResponse {
        val weekdays = jobPostingWeekdayService.findByJobPostingId(jobPostingId)?.map { it.weekday }
        val lifeAssistances = jobPostingLifeAssistanceService.findByJobPostingId(jobPostingId)
            ?.map { it.lifeAssistance }
        val applyMethods =
            jobPostingApplyMethodService.findByJobPostingId(jobPostingId)?.map { it.applyMethod }

        jobPostingService.getById(jobPostingId).let {
            return CenterJobPostingResponse(
                id = it.id,
                weekdays = weekdays!!,
                startTime = it.startTime,
                endTime = it.endTime,
                payType = it.payType,
                payAmount = it.payAmount,
                roadNameAddress = it.roadNameAddress,
                clientName = it.clientName,
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
            )
        }
    }

}
