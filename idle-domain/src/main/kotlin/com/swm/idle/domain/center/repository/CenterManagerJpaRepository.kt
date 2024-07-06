package com.swm.idle.domain.center.repository

import com.swm.idle.domain.center.entity.CenterManager
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CenterManagerJpaRepository : JpaRepository<CenterManager, UUID> {
}
