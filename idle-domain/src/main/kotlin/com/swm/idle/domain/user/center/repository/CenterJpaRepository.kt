package com.swm.idle.domain.user.center.repository

import com.swm.idle.domain.user.center.entity.jpa.Center
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CenterJpaRepository : JpaRepository<Center, UUID> {

    fun findByBusinessRegistrationNumber(businessRegistrationNumber: String): Center?

}