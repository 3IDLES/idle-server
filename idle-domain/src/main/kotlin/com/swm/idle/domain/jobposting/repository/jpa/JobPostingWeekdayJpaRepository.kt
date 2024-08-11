package com.swm.idle.domain.jobposting.repository.jpa

import com.swm.idle.domain.jobposting.entity.jpa.JobPostingWeekday
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JobPostingWeekdayJpaRepository : JpaRepository<JobPostingWeekday, UUID> {

    @Query(
        value =
        """
            SELECT *
            FROM job_posting_weekday as jw
            WHERE jw.entity_status = 'ACTIVE'
            AND jw.job_posting_id = :jobPostingId
        """,
        nativeQuery = true
    )
    fun findAllByJobPostingId(jobPostingId: UUID): List<JobPostingWeekday>?

}
