package com.swm.idle.domain.jobposting.repository.jpa

import com.swm.idle.domain.jobposting.entity.jpa.JobPostingLifeAssistance
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JobPostingLifeAssistanceJpaRepository : JpaRepository<JobPostingLifeAssistance, UUID> {

    fun findByJobPostingId(id: UUID): List<JobPostingLifeAssistance>?

}
