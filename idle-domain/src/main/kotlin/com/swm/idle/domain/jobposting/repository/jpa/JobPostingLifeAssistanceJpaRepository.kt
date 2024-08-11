package com.swm.idle.domain.jobposting.repository.jpa

import com.swm.idle.domain.jobposting.entity.jpa.JobPostingLifeAssistance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JobPostingLifeAssistanceJpaRepository : JpaRepository<JobPostingLifeAssistance, UUID> {

    @Query(
        value =
        """
            SELECT * 
            FROM job_posting_life_assistance as jpla
            WHERE jpla.entity_status = 'ACTIVE'
            AND jpla.job_posting_id = :jobPostingId
        """,
        nativeQuery = true
    )
    fun findAllByJobPostingId(jobPostingId: UUID): List<JobPostingLifeAssistance>?

}
