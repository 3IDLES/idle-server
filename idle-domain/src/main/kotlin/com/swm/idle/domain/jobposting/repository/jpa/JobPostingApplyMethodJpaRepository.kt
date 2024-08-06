package com.swm.idle.domain.jobposting.repository.jpa

import com.swm.idle.domain.jobposting.entity.jpa.JobPostingApplyMethod
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JobPostingApplyMethodJpaRepository : JpaRepository<JobPostingApplyMethod, UUID> {
}
