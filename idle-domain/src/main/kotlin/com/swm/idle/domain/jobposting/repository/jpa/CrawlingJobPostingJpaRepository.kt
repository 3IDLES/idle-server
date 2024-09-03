package com.swm.idle.domain.jobposting.repository.jpa

import com.swm.idle.domain.jobposting.entity.jpa.CrawledJobPosting
import io.lettuce.core.dynamic.annotation.Param
import org.locationtech.jts.geom.Point
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CrawlingJobPostingJpaRepository : JpaRepository<CrawledJobPosting, UUID> {

    override fun getById(id: UUID): CrawledJobPosting

    @Query(
        """
            SELECT ST_Distance(
                :carerLocation,
                :clientLocation
            )
        """,
        nativeQuery = true
    )
    fun calculateDistance(
        @Param("carerLocation") carerLocation: Point,
        @Param("clientLocation") clientLocation: Point,
    ): Double

}
