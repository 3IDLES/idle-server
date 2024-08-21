package com.swm.idle.application.jobposting.service.facade

import com.swm.idle.application.jobposting.service.domain.JobPostingFavoriteService
import org.springframework.stereotype.Service
import java.util.*

@Service
data class JobPostingFavoriteFacadeService(
    private val jobPostingFavoriteService: JobPostingFavoriteService,
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

}
