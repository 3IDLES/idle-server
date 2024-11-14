package com.swm.idle.application.applys.facade

import com.swm.idle.application.applys.domain.CarerApplyService
import com.swm.idle.application.applys.event.CarerApplyEventPublisher
import com.swm.idle.application.applys.vo.CarerApplyNotificationInfo
import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.jobposting.domain.JobPostingService
import com.swm.idle.application.notification.domain.DeviceTokenService
import com.swm.idle.application.notification.domain.NotificationService
import com.swm.idle.application.user.carer.domain.CarerService
import com.swm.idle.application.user.center.service.domain.CenterManagerService
import com.swm.idle.application.user.center.service.domain.CenterService
import com.swm.idle.domain.applys.event.ApplyEvent
import com.swm.idle.domain.applys.exception.ApplyException
import com.swm.idle.domain.common.enums.EntityStatus
import com.swm.idle.domain.jobposting.entity.jpa.JobPosting
import com.swm.idle.domain.jobposting.enums.ApplyMethodType
import com.swm.idle.domain.notification.enums.NotificationType
import com.swm.idle.domain.user.center.enums.CenterManagerAccountStatus
import com.swm.idle.domain.user.center.vo.BusinessRegistrationNumber
import com.swm.idle.domain.user.common.vo.BirthYear
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class CarerApplyFacadeService(
    private val carerApplyService: CarerApplyService,
    private val carerApplyEventPublisher: CarerApplyEventPublisher,
    private val deviceTokenService: DeviceTokenService,
    private val jobPostingService: JobPostingService,
    private val carerService: CarerService,
    private val centerManagerService: CenterManagerService,
    private val centerService: CenterService,
    private val notificationService: NotificationService,
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun createApply(
        jobPostingId: UUID,
        applyMethodType: ApplyMethodType,
    ) {
        val carer = getUserAuthentication().userId.let {
            carerService.getById(it)
        }
        val jobPosting = jobPostingService.getById(jobPostingId)

        if (carerApplyService.existsByJobPostingIdAndCarerId(jobPostingId, carer.id)) {
            throw ApplyException.AlreadyApplied()
        }

        if (jobPosting.isCompleted()) {
            throw ApplyException.JobPostingCompleted()
        }

        val centerManagers = centerService.getById(jobPosting.centerId).let {
            centerManagerService.findAllByCenterBusinessRegistrationNumber(
                BusinessRegistrationNumber(it.businessRegistrationNumber)
            )?.filter { centerManager ->
                centerManager.status == CenterManagerAccountStatus.APPROVED && centerManager.entityStatus == EntityStatus.ACTIVE
            }
        }

        centerManagers?.forEach { centerManager ->
            val deviceTokens = deviceTokenService.findAllByUserId(centerManager.id)

            val notificationInfo = CarerApplyNotificationInfo(
                title = "${carer.name} 님이 공고에 지원하였습니다.",
                body = createBodyMessage(jobPosting),
                receiverId = centerManager.id,
                notificationType = NotificationType.APPLICANT,
                imageUrl = carer.profileImageUrl,
                notificationDetails = mapOf(
                    "jobPostingId" to jobPostingId,
                )
            )

            val notification = notificationService.create(notificationInfo)

            deviceTokens?.forEach { deviceToken ->
                ApplyEvent.createApplyEvent(
                    deviceToken = deviceToken,
                    notificationId = notification.id,
                    notificationInfo = notificationInfo
                ).also {
                    carerApplyEventPublisher.publish(it)
                }
            }
        }

        carerApplyService.create(jobPostingId, carer.id, applyMethodType)
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

}
