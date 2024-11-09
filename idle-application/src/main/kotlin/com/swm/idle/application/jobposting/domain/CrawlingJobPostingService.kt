package com.swm.idle.application.jobposting.domain

import com.swm.idle.domain.common.dto.CrawlingJobPostingPreviewDto
import com.swm.idle.domain.common.exception.PersistenceException
import com.swm.idle.domain.jobposting.entity.jpa.CrawledJobPosting
import com.swm.idle.domain.jobposting.repository.jpa.CrawlingJobPostingJpaRepository
import com.swm.idle.domain.jobposting.repository.querydsl.CrawlingJobPostingSpatialQueryRepository
import org.locationtech.jts.geom.Point
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class CrawlingJobPostingService(
    private val crawlingJobPostingJpaRepository: CrawlingJobPostingJpaRepository,
    private val crawlingJobPostingSpatialQueryRepository: CrawlingJobPostingSpatialQueryRepository,
) {

    @Transactional
    fun saveAll(crawledJobPostings: List<CrawledJobPosting>) {
        crawlingJobPostingJpaRepository.saveAll(crawledJobPostings)
    }

    fun getById(crawlingJobPostingId: UUID): CrawledJobPosting {
        return crawlingJobPostingJpaRepository.findByIdOrNull(crawlingJobPostingId)
            ?: throw PersistenceException.ResourceNotFound("크롤링한 구인 공고(id=$crawlingJobPostingId)를 찾을 수 없습니다")
    }

    fun findAllByCarerLocationInRange(
        carerId: UUID,
        location: Point,
        next: UUID?,
        limit: Long,
    ): List<CrawlingJobPostingPreviewDto> {
        return crawlingJobPostingSpatialQueryRepository.findAllInRange(
            carerId = carerId,
            location = location,
            next = next,
            limit = limit,
        )
    }

    fun calculateDistance(
        crawledJobPosting: CrawledJobPosting,
        carerLocation: Point,
    ): Int {
        return crawlingJobPostingJpaRepository.calculateDistance(
            crawledJobPosting.location,
            carerLocation
        ).toInt()
    }

    fun findMyFavoritesByCarerId(carerId: UUID): List<CrawledJobPosting>? {
        return crawlingJobPostingJpaRepository.findAllFavoritesByCarerId(carerId)
    }

}
