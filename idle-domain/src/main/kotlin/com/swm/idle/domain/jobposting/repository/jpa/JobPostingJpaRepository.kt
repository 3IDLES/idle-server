package com.swm.idle.domain.jobposting.repository.jpa

import com.swm.idle.domain.jobposting.entity.jpa.JobPosting
import io.lettuce.core.dynamic.annotation.Param
import org.locationtech.jts.geom.Point
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JobPostingJpaRepository : JpaRepository<JobPosting, UUID> {

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

    @Query(
        """
            SELECT *
            FROM job_posting as jp
            WHERE jp.job_posting_status = 'IN_PROGRESS'
            AND jp.entity_status = 'ACTIVE'
            AND jp.center_id = :centerId
        """,
        nativeQuery = true
    )
    fun findAllInProgress(
        @Param("centerId") centerId: UUID,
    ): List<JobPosting>

}
