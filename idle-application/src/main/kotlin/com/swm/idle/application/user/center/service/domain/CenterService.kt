package com.swm.idle.application.user.center.service.domain

import com.swm.idle.domain.user.center.entity.jpa.Center
import com.swm.idle.domain.user.center.repository.CenterJpaRepository
import com.swm.idle.domain.user.center.vo.BusinessRegistrationNumber
import org.springframework.stereotype.Service

@Service
class CenterService(
    private val centerJpaRepository: CenterJpaRepository,
) {

    fun findByBusinessRegistrationNumber(businessRegistrationNumber: BusinessRegistrationNumber): Center? {
        return centerJpaRepository.findByBusinessRegistrationNumber(businessRegistrationNumber.value)
    }

}