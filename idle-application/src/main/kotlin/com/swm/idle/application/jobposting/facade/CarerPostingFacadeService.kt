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
import com.swm.idle.domain.common.enums.EntityStatus
import com.swm.idle.support.transfer.common.CursorScrollRequest
import com.swm.idle.support.transfer.jobposting.carer.CarerAppliedJobPostingScrollResponse
import com.swm.idle.support.transfer.jobposting.carer.CarerJobPostingResponse
import com.swm.idle.support.transfer.jobposting.carer.CarerJobPostingScrollResponse
import com.swm.idle.support.transfer.jobposting.carer.JobPostingFavoriteResponse
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Service
import java.util.*

@Service
class CarerPostingFacadeService(
    private val jobPostingWeekdayService: JobPostingWeekdayService,
    private val jobPostingLifeAssistanceService: JobPostingLifeAssistanceService,
    private val jobPostingApplyMethodService: JobPostingApplyMethodService,
    private val jobPostingService: JobPostingService,
    private val centerService: CenterService,
    private val carerService: CarerService,
    private val carerApplyService: CarerApplyService,
    private val jobPostingFavoriteService: JobPostingFavoriteService,
) {

    /**
     * Retrieves detailed information about a specific job posting for the authenticated carer.
     *
     * Assembles job posting details including associated weekdays, life assistance types, apply methods, center information, distance from the carer, application status, and favorite status.
     *
     * @param jobPostingId The unique identifier of the job posting to retrieve.
     * @return A response containing comprehensive job posting details tailored to the authenticated carer.
     */
    fun getJobPostingDetail(jobPostingId: UUID): CarerJobPostingResponse {
        val carer = carerService.getById(getUserAuthentication().userId)
        val posting = jobPostingService.getById(jobPostingId)

        val distance = jobPostingService.calculateDistance(
            posting,
            PointConverter.convertToPoint(carer)
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

        val center = centerService.getById(posting.centerId)

        return CarerJobPostingResponse.of(
            jobPosting = posting,
            weekdays = weekdays,
            lifeAssistances = lifeAssistances,
            applyMethods = applyMethods,
            center = center,
            distance = distance,
            applyTime = applyInfo?.createdAt,
            isFavorite = jobPostingFavorite != null && jobPostingFavorite.entityStatus == EntityStatus.ACTIVE,
        )
    }

    /**
     * Retrieves a paginated list of job postings within range of the authenticated carer's location.
     *
     * Uses the carer's current location to find nearby job postings and returns them in a scrollable response format.
     *
     * @param request Cursor-based pagination request containing the next cursor and limit.
     * @return A scroll response containing job postings near the carer, the next cursor, and the total count in the current page.
     */
    fun getJobPostingsInRange(
        request: CursorScrollRequest,
    ): CarerJobPostingScrollResponse {
        val carer = carerService.getById(getUserAuthentication().userId)
        val location = PointConverter.convertToPoint(carer)

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

    /**
     * Retrieves a paginated list of job posting previews within range of the specified location for the authenticated carer.
     *
     * Calculates the distance from each job posting to the carer's location and determines the next pagination cursor.
     *
     * @param location The geographic point to search from.
     * @param next The pagination cursor indicating the starting point for the next page, or null to start from the beginning.
     * @param limit The maximum number of job postings to return.
     * @return A pair containing the list of job posting previews (with distances set) and the next cursor UUID, or null if there are no more results.
     */
    private fun scrollByCarerLocationInRange(
        location: Point,
        next: UUID?,
        limit: Long,
    ): Pair<List<JobPostingPreviewDto>, UUID?> {
        val carer = getUserAuthentication().userId.let {
            carerService.getById(it)
        }

        val jobPostingPreviewDtos = jobPostingService.findAllByCarerLocationInRange(
            carerId = carer.id,
            location = location,
            next = next,
            limit = limit + 1,
        )

        val carerLocation = PointConverter.convertToPoint(carer)

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

    /**
     * Retrieves a paginated list of job postings that the authenticated carer has applied to.
     *
     * @param request The cursor-based pagination request.
     * @return A scroll response containing applied job postings, the next pagination cursor, and the total count.
     */
    fun getAppliedJobPostings(request: CursorScrollRequest): CarerAppliedJobPostingScrollResponse {
        val (items, next) = scrollByCarerApplyHistory(
            next = request.next,
            limit = request.limit,
            carerId = getUserAuthentication().userId
        )

        return CarerAppliedJobPostingScrollResponse.from(
            items = items,
            next = next,
            total = items.size,
        )
    }

    /**
     * Retrieves a paginated list of job postings the specified carer has applied to, including distance from the carer's current location.
     *
     * @param carerId The unique identifier of the carer whose application history is being queried.
     * @param next The pagination cursor indicating the starting point for the next page, or null to start from the beginning.
     * @param limit The maximum number of job postings to return.
     * @return A pair containing the list of job posting previews (with distance calculated) and the next pagination cursor, or null if there are no more results.
     */
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

        val carer = carerService.getById(getUserAuthentication().userId)
        val carerLocation = PointConverter.convertToPoint(carer)

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

    /**
     * Retrieves all job postings favorited by the authenticated carer, including the distance from the carer's current location.
     *
     * @return A response containing the list of favorite job postings with distance information.
     */
    fun getMyFavoriteJobPostings(): JobPostingFavoriteResponse {
        val carer = carerService.getById(getUserAuthentication().userId)
        val location = PointConverter.convertToPoint(carer)

        val jobPostingPreviewDtos: List<JobPostingPreviewDto>? =
            jobPostingService.findAllFavorites(carer.id)

        jobPostingPreviewDtos?.map { jobPostingPreviewDto ->
            val distance = jobPostingService.calculateDistance(
                jobPostingPreviewDto.jobPosting,
                location
            )

            JobPostingFavoriteResponse.MyFavoriteJobPostingDto.of(
                jobPostingPreviewDto = jobPostingPreviewDto,
                distance = distance,
            )
        }.let {
            return JobPostingFavoriteResponse.from(it!!)
        }
    }

}
