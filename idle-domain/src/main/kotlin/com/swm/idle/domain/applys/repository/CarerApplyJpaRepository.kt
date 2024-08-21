package com.swm.idle.domain.applys.repository

import com.swm.idle.domain.applys.entity.jpa.Applys
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CarerApplyJpaRepository : JpaRepository<Applys, UUID> {

    fun existsByJobPostingIdAndCarerId(
        jobPostingId: UUID,
        carerId: UUID,
    ): Boolean

    fun findAllByJobPostingId(jobPostingId: UUID): List<Applys>

    fun countByJobPostingId(jobPostingId: UUID): Int

}
