package com.swm.idle.domain.apply.repository

import com.swm.idle.domain.apply.entity.jpa.Apply
import com.swm.idle.domain.jobposting.vo.ApplyMethodType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CarerApplyJpaRepository : JpaRepository<Apply, UUID> {

    fun existsByJobPostingIdAndCarerIdAndApplyMethodType(
        jobPostingId: UUID,
        carerId: UUID,
        applyMethodType: ApplyMethodType,
    ): Boolean

}
