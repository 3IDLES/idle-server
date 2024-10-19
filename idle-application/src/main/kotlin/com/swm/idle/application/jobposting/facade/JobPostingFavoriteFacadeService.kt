package com.swm.idle.application.jobposting.facade

import com.swm.idle.application.jobposting.domain.JobPostingFavoriteService
import com.swm.idle.application.jobposting.domain.JobPostingService
import com.swm.idle.application.user.carer.domain.CarerService
import com.swm.idle.domain.jobposting.enums.JobPostingType
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
        jobPostingType: JobPostingType,
    ) {
        jobPostingFavoriteService.findByJobPostingIdAndCarerId(
            jobPostingId = jobPostingId,
            carerId = carerId,
        )?.let {
            it.active()
        } ?: jobPostingFavoriteService.create(
            jobPostingId = jobPostingId,
            carerId = carerId,
            jobPostingType = jobPostingType,
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

}
