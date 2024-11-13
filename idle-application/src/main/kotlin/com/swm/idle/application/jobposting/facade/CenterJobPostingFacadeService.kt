package com.swm.idle.application.jobposting.facade

import com.swm.idle.application.applys.domain.ApplicantService
import com.swm.idle.application.applys.domain.CarerApplyService
import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.jobposting.domain.JobPostingApplyMethodService
import com.swm.idle.application.jobposting.domain.JobPostingLifeAssistanceService
import com.swm.idle.application.jobposting.domain.JobPostingService
import com.swm.idle.application.jobposting.domain.JobPostingWeekdayService
import com.swm.idle.application.jobposting.event.CreateJobPostingEventPublisher
import com.swm.idle.application.jobposting.vo.CreateJobPostingNotificationInfo
import com.swm.idle.application.jobposting.vo.JobPostingInfo
import com.swm.idle.application.notification.domain.DeviceTokenService
import com.swm.idle.application.notification.domain.NotificationService
import com.swm.idle.application.user.carer.domain.CarerService
import com.swm.idle.application.user.center.service.domain.CenterManagerService
import com.swm.idle.application.user.center.service.domain.CenterService
import com.swm.idle.application.user.common.service.domain.DeletedUserInfoService
import com.swm.idle.domain.jobposting.entity.jpa.JobPosting
import com.swm.idle.domain.jobposting.event.CreateJobPostingEvent
import com.swm.idle.domain.notification.enums.NotificationType
import com.swm.idle.domain.user.carer.entity.jpa.Carer
import com.swm.idle.domain.user.center.exception.CenterException
import com.swm.idle.domain.user.center.vo.BusinessRegistrationNumber
import com.swm.idle.domain.user.common.vo.BirthYear
import com.swm.idle.infrastructure.client.geocode.service.GeoCodeService
import com.swm.idle.support.transfer.jobposting.center.CenterJobPostingResponse
import com.swm.idle.support.transfer.jobposting.center.CreateJobPostingRequest
import com.swm.idle.support.transfer.jobposting.center.JobPostingApplicantCountResponse
import com.swm.idle.support.transfer.jobposting.center.JobPostingApplicantsResponse
import com.swm.idle.support.transfer.jobposting.center.UpdateJobPostingRequest
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class CenterJobPostingFacadeService(
    private val jobPostingService: JobPostingService,
    private val jobPostingLifeAssistanceService: JobPostingLifeAssistanceService,
    private val jobPostingWeekdayService: JobPostingWeekdayService,
    private val jobPostingApplyMethodService: JobPostingApplyMethodService,
    private val geoCodeService: GeoCodeService,
    private val centerManagerService: CenterManagerService,
    private val centerService: CenterService,
    private val carerService: CarerService,
    private val carerApplyService: CarerApplyService,
    private val applicantService: ApplicantService,
    private val deletedUserInfoService: DeletedUserInfoService,
    private val deviceTokenService: DeviceTokenService,
    private val notificationService: NotificationService,
    private val createJobPostingEventPublisher: CreateJobPostingEventPublisher,
) {

    @Transactional
    suspend fun create(request: CreateJobPostingRequest) {
        val centerId =
            centerManagerService.getById(getUserAuthentication().userId).let {
                centerService.findByBusinessRegistrationNumber(
                    BusinessRegistrationNumber(it.centerBusinessRegistrationNumber)
                )?.id
            } ?: throw CenterException.NotFoundException()

        val geoCodeSearchResult = geoCodeService.search(request.roadNameAddress)

        val jobPosting = jobPostingService.create(
            centerId = centerId,
            jobPostingInfo = JobPostingInfo.of(
                request = request,
                latitude = geoCodeSearchResult.addresses[0].y,
                longitude = geoCodeSearchResult.addresses[0].x
            )
        )

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

        val carers = carerService.findAllByLocationWithinRadius(jobPosting.location)

        carers?.forEach { carer ->
            val deviceTokens = deviceTokenService.findAllByUserId(carer.id)

            val notificationInfo = CreateJobPostingNotificationInfo(
                title = "${carer.name} 님이 공고에 지원하였습니다.",
                body = createBodyMessage(jobPosting),
                receiverId = carer.id,
                notificationType = NotificationType.NEW_JOB_POSTING,
                imageUrl = carer.profileImageUrl,
                notificationDetails = mapOf(
                    "jobPostingId" to jobPosting.id,
                )
            )

            val notification = notificationService.create(notificationInfo)

            deviceTokens?.forEach { deviceToken ->
                CreateJobPostingEvent.of(
                    deviceToken = deviceToken,
                    notificationId = notification.id,
                    notificationInfo = notificationInfo
                ).also {
                    createJobPostingEventPublisher.publish(it)
                }
            }
        }
    }

    private fun createBodyMessage(jobPosting: JobPosting): String {
        val filteredLotNumberAddress = jobPosting.lotNumberAddress.split(" ")
            .take(3)
            .joinToString(" ")

        return "$filteredLotNumberAddress " +
                "${jobPosting.careLevel}등급 " +
                "${BirthYear.calculateAge(jobPosting.birthYear)}세 " +
                jobPosting.gender.value
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

        request.applyMethod.let {
            jobPostingApplyMethodService.update(
                jobPosting = jobPosting,
                applyMethods = request.applyMethod,
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
    fun updateToCompleted(jobPostingId: UUID) {
        jobPostingService.getById(jobPostingId).let {
            jobPostingService.updateToCompleted(it)
        }
    }

    fun getJobPostingDetail(jobPostingId: UUID): CenterJobPostingResponse {
        val weekdays = jobPostingWeekdayService.findByJobPostingId(jobPostingId)?.map { it.weekday }
        val lifeAssistances = jobPostingLifeAssistanceService.findByJobPostingId(jobPostingId)
            ?.map { it.lifeAssistance }
        val applyMethods =
            jobPostingApplyMethodService.findByJobPostingId(jobPostingId)?.map { it.applyMethod }

        jobPostingService.getById(jobPostingId).let {
            return CenterJobPostingResponse.of(
                jobPosting = it,
                weekdays = weekdays,
                lifeAssistances = lifeAssistances,
                applyMethods = applyMethods,
            )
        }
    }

    fun findAllInProgressById(): List<JobPosting> {
        val center = getUserAuthentication().userId.let { centerManagerId ->
            centerManagerService.getById(centerManagerId).let {
                centerService.findByBusinessRegistrationNumber(
                    BusinessRegistrationNumber(it.centerBusinessRegistrationNumber)
                ) ?: throw CenterException.NotFoundException()
            }
        }

        return jobPostingService.findAllInProgress(center.id)
    }

    fun findAllCompletedById(): List<JobPosting> {
        val center = getUserAuthentication().userId.let { centerManagerId ->
            centerManagerService.getById(centerManagerId).let {
                centerService.findByBusinessRegistrationNumber(
                    BusinessRegistrationNumber(it.centerBusinessRegistrationNumber)
                ) ?: throw CenterException.NotFoundException()
            }
        }

        return jobPostingService.findAllCompleted(center.id)
    }

    fun getJobPostingApplicants(jobPostingId: UUID): JobPostingApplicantsResponse {
        val applys = carerApplyService.findAllByJobPostingId(jobPostingId)
        val applicants: List<Carer> = applys.mapNotNull { apply ->
            carerService.getById(apply.carerId).takeIf {
                deletedUserInfoService.findById(apply.carerId) == null
            }
        }

        val jobPosting = jobPostingService.getById(jobPostingId)

        return JobPostingApplicantsResponse.of(
            jobPosting = jobPosting,
            applicants = applicants,
        )
    }

    fun getApplicantCount(jobPostingId: UUID): JobPostingApplicantCountResponse {
        return JobPostingApplicantCountResponse(applicantService.countByJobPostingId(jobPostingId))
    }

}
