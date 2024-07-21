package com.swm.idle.domain.user.carer.repository.jpa

import com.swm.idle.domain.user.carer.entity.Carer
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CarerJpaRepository : JpaRepository<Carer, UUID> {

    fun findByPhoneNumber(value: String): Carer?

}
