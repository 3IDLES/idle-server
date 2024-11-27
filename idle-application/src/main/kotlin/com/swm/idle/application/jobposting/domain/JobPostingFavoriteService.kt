package com.swm.idle.application.jobposting.domain

import com.swm.idle.domain.common.exception.PersistenceException
import com.swm.idle.domain.jobposting.entity.jpa.JobPostingFavorite
import com.swm.idle.domain.jobposting.enums.JobPostingType
import com.swm.idle.domain.jobposting.repository.jpa.JobPostingFavoriteJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class JobPostingFavoriteService(
    private val jobPostingFavoriteJpaRepository: JobPostingFavoriteJpaRepository,
) {

    @Transactional
    fun create(
        jobPostingId: UUID,
        carerId: UUID,
        jobPostingType: JobPostingType,
    ) {
        val jobPostingFavorite = jobPostingFavoriteJpaRepository.findByJobPostingIdAndCarerId(
            jobPostingId = jobPostingId,
            carerId = carerId
        )?.let {
            it.active()
        } ?: jobPostingFavoriteJpaRepository.save(
            JobPostingFavorite(
                carerId = carerId,
                jobPostingId = jobPostingId,
                jobPostingType = jobPostingType,
            )
        )
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

    fun findByJobPostingIdAndCarerId(jobPostingId: UUID, carerId: UUID): JobPostingFavorite? {
        return jobPostingFavoriteJpaRepository.findByJobPostingIdAndCarerId(
            jobPostingId = jobPostingId,
            carerId = carerId,
        )
    }

    fun findByByJobPostingId(jobPostingId: UUID): JobPostingFavorite? {
        return jobPostingFavoriteJpaRepository.findByJobPostingId(jobPostingId)
    }

}
