package com.swm.idle.application.jobposting.facade

import com.swm.idle.application.applys.domain.CarerApplyService
import com.swm.idle.application.common.converter.PointConverter
import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.jobposting.domain.JobPostingApplyMethodService
import com.swm.idle.application.jobposting.domain.JobPostingFavoriteService
import com.swm.idle.application.jobposting.domain.JobPostingLifeAssistanceService
import com.swm.idle.application.jobposting.domain.JobPostingService
import com.swm.idle.application.jobposting.domain.JobPostingWeekdayService
import com.swm.idle.application.user.carer.domain.CarerService
import com.swm.idle.application.user.center.service.domain.CenterService
import com.swm.idle.domain.common.dto.JobPostingPreviewDto
import com.swm.idle.support.transfer.jobposting.carer.CarerAppliedJobPostingScrollResponse
import com.swm.idle.support.transfer.jobposting.carer.CarerJobPostingResponse
import com.swm.idle.support.transfer.jobposting.carer.CarerJobPostingScrollResponse
import com.swm.idle.support.transfer.jobposting.carer.CursorScrollRequest
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Service
import java.util.*

@Service
class CarerJobPostingFacadeService(
    private val jobPostingWeekdayService: JobPostingWeekdayService,
    private val jobPostingLifeAssistanceService: JobPostingLifeAssistanceService,
    private val jobPostingApplyMethodService: JobPostingApplyMethodService,
    private val jobPostingService: JobPostingService,
    private val centerService: CenterService,
    private val carerService: CarerService,
    private val carerApplyService: CarerApplyService,
    private val jobPostingFavoriteService: JobPostingFavoriteService,
) {

    fun getJobPostingDetail(jobPostingId: UUID): CarerJobPostingResponse {
        val carer = getUserAuthentication().userId.let {
            carerService.getById(it)
        }

        val jobPosting = jobPostingService.getById(jobPostingId)

        val distance = jobPostingService.calculateDistance(
            jobPosting,
            PointConverter.convertToPoint(
                latitude = carer.latitude.toDouble(),
                longitude = carer.longitude.toDouble(),
            )
        )

        val weekdays = jobPostingWeekdayService.findByJobPostingId(jobPostingId)?.map { it.weekday }
        val lifeAssistances = jobPostingLifeAssistanceService.findByJobPostingId(jobPostingId)
            ?.map { it.lifeAssistance }
        val applyMethods =
            jobPostingApplyMethodService.findByJobPostingId(jobPostingId)?.map { it.applyMethod }

        val applyInfo = carerApplyService.findByJobPostingIdAndCarerId(
            jobPostingId = jobPostingId,
            carerId = carer.id,
        )

        val jobPostingFavorite = jobPostingFavoriteService.findByJobPostingIdAndCarerId(
            jobPostingId = jobPostingId,
            carerId = carer.id,
        )

        val center = centerService.getById(jobPosting.centerId)

        return CarerJobPostingResponse.of(
            jobPosting = jobPosting,
            weekdays = weekdays,
            lifeAssistances = lifeAssistances,
            applyMethods = applyMethods,
            center = center,
            distance = distance,
            applyTime = applyInfo?.createdAt,
            isFavorite = jobPostingFavorite != null,
        )
    }

    fun getJobPostingsInRange(
        request: CursorScrollRequest,
        location: Point,
    ): CarerJobPostingScrollResponse {
        val (items, next) = scrollByCarerLocationInRange(
            location = location,
            next = request.next,
            limit = request.limit
        )

        return CarerJobPostingScrollResponse.from(
            items = items,
            next = next,
            total = items.size,
        )
    }

    private fun scrollByCarerLocationInRange(
        location: Point,
        next: UUID?,
        limit: Long,
    ): Pair<List<JobPostingPreviewDto>, UUID?> {
        val jobPostingPreviewDtos = jobPostingService.findAllByCarerLocationInRange(
            location = location,
            next = next,
            limit = limit + 1,
        )

        val carerLocation = getUserAuthentication().userId.let {
            carerService.getById(it)
        }.let {
            PointConverter.convertToPoint(
                latitude = it.latitude.toDouble(),
                longitude = it.longitude.toDouble(),
            )
        }

        for (jobPostingPreviewDto in jobPostingPreviewDtos) {
            jobPostingPreviewDto.distance = jobPostingService.calculateDistance(
                jobPostingPreviewDto.jobPosting,
                carerLocation
            )
        }

        val newNext =
            if (jobPostingPreviewDtos.size > limit) jobPostingPreviewDtos.last().jobPosting.id else null
        val items =
            if (newNext == null) jobPostingPreviewDtos else jobPostingPreviewDtos.subList(
                0,
                limit.toInt()
            )
        return items to newNext
    }

    fun getAppliedJobPostings(
        request: CursorScrollRequest,
        carerId: UUID,
    ): CarerAppliedJobPostingScrollResponse {
        val (items, next) = scrollByCarerApplyHistory(
            next = request.next,
            limit = request.limit,
            carerId = carerId,
        )

        return CarerAppliedJobPostingScrollResponse.from(
            items = items,
            next = next,
            total = items.size,
        )
    }

    private fun scrollByCarerApplyHistory(
        carerId: UUID,
        next: UUID?,
        limit: Long,
    ): Pair<List<JobPostingPreviewDto>, UUID?> {

        val JobPostingPreviewDtos = jobPostingService.findAllByCarerApplyHistory(
            next = next,
            limit = limit + 1,
            carerId = carerId,
        )

        val carerLocation = getUserAuthentication().userId.let {
            carerService.getById(it)
        }.let {
            PointConverter.convertToPoint(
                latitude = it.latitude.toDouble(),
                longitude = it.longitude.toDouble(),
            )
        }

        for (jobPostingPreviewDto in JobPostingPreviewDtos) {
            jobPostingService.calculateDistance(
                jobPostingPreviewDto.jobPosting,
                carerLocation
            ).also { jobPostingPreviewDto.distance = it }
        }

        val newNext =
            if (JobPostingPreviewDtos.size > limit) JobPostingPreviewDtos.last().jobPosting.id else null
        val items =
            if (newNext == null) JobPostingPreviewDtos else JobPostingPreviewDtos.subList(
                0,
                limit.toInt()
            )
        return items to newNext
    }

}

