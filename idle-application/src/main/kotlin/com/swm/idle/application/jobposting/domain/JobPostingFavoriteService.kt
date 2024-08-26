package com.swm.idle.application.jobposting.domain

import com.swm.idle.domain.common.dto.FavoriteJobPostingWithWeekdaysDto
import com.swm.idle.domain.common.exception.PersistenceException
import com.swm.idle.domain.jobposting.entity.jpa.JobPostingFavorite
import com.swm.idle.domain.jobposting.repository.jpa.JobPostingFavoriteJpaRepository
import com.swm.idle.domain.jobposting.repository.querydsl.JobPostingQueryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class JobPostingFavoriteService(
    private val jobPostingFavoriteJpaRepository: JobPostingFavoriteJpaRepository,
    private val jobPostingQueryRepository: JobPostingQueryRepository,
) {

    @Transactional
    fun create(
        jobPostingId: UUID,
        carerId: UUID,
    ) {
        val jobPostingFavorite = jobPostingFavoriteJpaRepository.findByJobPostingIdAndCarerId(
            jobPostingId = jobPostingId,
            carerId = carerId
        ) ?: jobPostingFavoriteJpaRepository.save(
            JobPostingFavorite(
                carerId = carerId,
                jobPostingId = jobPostingId,
            )
        )

        jobPostingFavorite.active()
    }

    @Transactional
    fun delete(
        jobPostingId: UUID,
        carerId: UUID,
    ) {
        val jobPostingFavorite = jobPostingFavoriteJpaRepository.findByJobPostingIdAndCarerId(
            jobPostingId = jobPostingId,
            carerId = carerId
        ) ?: throw PersistenceException.ResourceNotFound("해당 공고 즐겨찾기 내역이 존재하지 않습니다.")

        jobPostingFavorite.delete()
    }

    fun findMyFavoriteJobPostingsByCarerId(
        carerId: UUID,
        next: UUID?,
        limit: Long,
    ): List<FavoriteJobPostingWithWeekdaysDto> {
        return jobPostingQueryRepository.findAllInFavorites(
            carerId = carerId,
            next = next,
            limit = limit
        )
    }

}
