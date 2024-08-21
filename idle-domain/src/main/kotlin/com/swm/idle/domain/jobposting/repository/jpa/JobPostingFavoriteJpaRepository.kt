package com.swm.idle.domain.jobposting.repository.jpa

import com.swm.idle.domain.jobposting.entity.jpa.JobPostingFavorite
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JobPostingFavoriteJpaRepository : JpaRepository<JobPostingFavorite, UUID> {

    fun save(jobPostingFavorite: JobPostingFavorite): JobPostingFavorite

    fun findByJobPostingIdAndCarerId(
        jobPostingId: UUID,
        carerId: UUID,
    ): JobPostingFavorite?

}
