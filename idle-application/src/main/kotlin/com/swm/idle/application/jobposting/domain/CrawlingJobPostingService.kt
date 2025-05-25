package com.swm.idle.application.jobposting.domain

import com.swm.idle.domain.common.dto.CrawlingJobPostingPreviewDto
import com.swm.idle.domain.common.exception.PersistenceException
import com.swm.idle.domain.jobposting.entity.jpa.CrawledJobPosting
import com.swm.idle.domain.jobposting.repository.jpa.CrawlingJobPostingJpaRepository
import com.swm.idle.domain.jobposting.repository.redis.RedisJobPostingRepository
import org.locationtech.jts.geom.Point
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class CrawlingJobPostingService(
    private val crawlingJobPostingJpaRepository: CrawlingJobPostingJpaRepository,
    private val redisJobPostingRepository: RedisJobPostingRepository,
) {

    /**
     * Retrieves a crawled job posting by its unique identifier.
     *
     * @param crawlingJobPostingId The UUID of the job posting to retrieve.
     * @return The corresponding CrawledJobPosting entity.
     * @throws PersistenceException.ResourceNotFound if the job posting does not exist.
     */
    fun getById(crawlingJobPostingId: UUID): CrawledJobPosting {
        return crawlingJobPostingJpaRepository.findByIdOrNull(crawlingJobPostingId)
            ?: throw PersistenceException.ResourceNotFound("크롤링한 구인 공고(id=$crawlingJobPostingId)를 찾을 수 없습니다")
    }

    /**
     * Retrieves a list of job posting previews within a specified distance from a given location.
     *
     * Queries Redis for job posting IDs near the provided location within the given distance, fetches the corresponding postings from the database, and returns preview DTOs for each posting.
     *
     * @param next Optional UUID for pagination; results start after this posting if provided.
     * @param location The geographic point from which to search.
     * @param distance The maximum distance from the location, in meters.
     * @param limit The maximum number of results to return.
     * @return A list of job posting preview DTOs within the specified range.
     */
    fun findAllInRange(
        next: UUID?,
        location: Point,
        distance: Long,
        limit: Long
    ): List<CrawlingJobPostingPreviewDto> {
        val postingIds = redisJobPostingRepository.findByLocationAndDistance(location, distance, limit, next)
        val postings = crawlingJobPostingJpaRepository.findAllById(postingIds)
        return postings.map { CrawlingJobPostingPreviewDto(it, distance.toInt()) }
    }


    /**
     * Calculates the distance in meters between a job posting's location and a specified carer's location.
     *
     * @param crawledJobPosting The job posting whose location is used as the starting point.
     * @param carerLocation The geographic location to measure the distance to.
     * @return The distance in meters as an integer.
     */
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
