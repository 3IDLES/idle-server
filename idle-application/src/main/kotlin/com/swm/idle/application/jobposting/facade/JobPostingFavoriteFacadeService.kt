package com.swm.idle.application.jobposting.facade

import com.swm.idle.application.common.converter.PointConverter
import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.jobposting.domain.JobPostingFavoriteService
import com.swm.idle.application.jobposting.domain.JobPostingService
import com.swm.idle.application.user.carer.domain.CarerService
import com.swm.idle.domain.common.dto.FavoriteJobPostingWithWeekdaysDto
import com.swm.idle.support.transfer.jobposting.carer.CursorScrollRequest
import com.swm.idle.support.transfer.jobposting.carer.GetMyFavoriteJobPostingScrollResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
data class JobPostingFavoriteFacadeService(
    private val jobPostingFavoriteService: JobPostingFavoriteService,
    private val carerService: CarerService,
    private val jobPostingService: JobPostingService,
) {

    fun createJobPostingFavorite(
        jobPostingId: UUID,
        carerId: UUID,
    ) {
        jobPostingFavoriteService.create(
            jobPostingId = jobPostingId,
            carerId = carerId,
        )
    }

    fun deleteJobPostingFavorite(
        jobPostingId: UUID,
        carerId: UUID,
    ) {
        jobPostingFavoriteService.delete(
            jobPostingId = jobPostingId,
            carerId = carerId,
        )
    }

    fun getMyFavoriteJobPostings(
        request: CursorScrollRequest,
        carerId: UUID,
    ): GetMyFavoriteJobPostingScrollResponse {
        val (items, next) = scrollByCarerFavorites(
            carerId = carerId,
            next = request.next,
            limit = request.limit
        )

        return GetMyFavoriteJobPostingScrollResponse.from(
            items = items,
            next = next,
            total = items.size,
        )
    }

    fun scrollByCarerFavorites(
        carerId: UUID,
        next: UUID?,
        limit: Long,
    ): Pair<List<FavoriteJobPostingWithWeekdaysDto>, UUID?> {

        val FavoriteJobPostingWithWeekdaysDtos =
            jobPostingFavoriteService.findMyFavoriteJobPostingsByCarerId(
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

        for (favoriteJobPostingWithWeekdaysDto in FavoriteJobPostingWithWeekdaysDtos) {
            jobPostingService.calculateDistance(
                favoriteJobPostingWithWeekdaysDto.jobPosting,
                carerLocation
            ).also { favoriteJobPostingWithWeekdaysDto.distance = it }
        }

        val newNext =
            if (FavoriteJobPostingWithWeekdaysDtos.size > limit) FavoriteJobPostingWithWeekdaysDtos.last().jobPosting.id else null
        val items =
            if (newNext == null) FavoriteJobPostingWithWeekdaysDtos else FavoriteJobPostingWithWeekdaysDtos.subList(
                0,
                limit.toInt()
            )
        return items to newNext
    }

}
