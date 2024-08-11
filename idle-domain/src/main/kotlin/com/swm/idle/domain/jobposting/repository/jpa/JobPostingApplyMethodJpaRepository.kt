package com.swm.idle.domain.jobposting.repository.jpa

import com.swm.idle.domain.jobposting.entity.jpa.JobPostingApplyMethod
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JobPostingApplyMethodJpaRepository : JpaRepository<JobPostingApplyMethod, UUID> {

    @Query(
        value =
        """
            SELECT *
            FROM job_posting_apply_method as jpam
            WHERE jpam.entity_status = 'ACTIVE'
            AND jpam.job_posting_id = :jobPostingId
        """,
        nativeQuery = true
    )
    fun findAllByJobPostingId(jobPostingId: UUID): List<JobPostingApplyMethod>?

}
