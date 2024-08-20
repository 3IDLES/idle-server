package com.swm.idle.domain.applys.repository

import com.swm.idle.domain.applys.entity.jpa.Applys
import com.swm.idle.domain.jobposting.vo.ApplyMethodType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CarerApplyJpaRepository : JpaRepository<Applys, UUID> {

    fun existsByJobPostingIdAndCarerIdAndApplyMethodType(
        jobPostingId: UUID,
        carerId: UUID,
        applyMethodType: ApplyMethodType,
    ): Boolean

}
