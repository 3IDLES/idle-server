package com.swm.idle.application.jobposting.service.facade

import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.jobposting.service.domain.JobPostingApplyMethodService
import com.swm.idle.application.jobposting.service.domain.JobPostingLifeAssistanceService
import com.swm.idle.application.jobposting.service.domain.JobPostingService
import com.swm.idle.application.jobposting.service.domain.JobPostingWeekdayService
import com.swm.idle.application.jobposting.service.vo.JobPostingInfo
import com.swm.idle.support.transfer.jobposting.CreateJobPostingRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class JobPostingFacadeService(
    private val jobPostingService: JobPostingService,
    private val jobPostingLifeAssistanceService: JobPostingLifeAssistanceService,
    private val jobPostingWeekdayService: JobPostingWeekdayService,
    private val jobPostingApplyMethodService: JobPostingApplyMethodService,
) {

    @Transactional
    fun create(request: CreateJobPostingRequest) {
        val centerId = getUserAuthentication().userId

        jobPostingService.create(
            centerId = centerId,
            jobPostingInfo = JobPostingInfo.of(request)
        ).let { jobPosting ->
            request.lifeAssistance?.let {
                jobPostingLifeAssistanceService.create(
                    jobPostingId = jobPosting.id,
                    lifeAssistance = request.lifeAssistance!!,
                )
            }

            jobPostingWeekdayService.create(
                jobPostingId = jobPosting.id,
                weekdays = request.weekdays,
            )
            jobPostingApplyMethodService.create(
                jobPostingId = jobPosting.id,
                applyMethods = request.applyMethod,
            )
        }
    }

}
