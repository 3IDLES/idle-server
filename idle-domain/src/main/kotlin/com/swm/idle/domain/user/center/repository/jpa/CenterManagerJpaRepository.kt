package com.swm.idle.domain.user.center.repository.jpa

import com.swm.idle.domain.user.center.entity.jpa.CenterManager
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CenterManagerJpaRepository : JpaRepository<CenterManager, UUID> {

    fun findByIdentifier(value: String): CenterManager?

    fun existsByIdentifier(value: String): Boolean

    fun findByPhoneNumber(value: String): CenterManager?

}
