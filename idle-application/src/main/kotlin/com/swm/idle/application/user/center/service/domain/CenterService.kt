package com.swm.idle.application.user.center.service.domain

import com.swm.idle.domain.user.center.entity.jpa.Center
import com.swm.idle.domain.user.center.repository.CenterJpaRepository
import com.swm.idle.domain.user.center.vo.BusinessRegistrationNumber
import com.swm.idle.support.common.uuid.UuidCreator
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class CenterService(
    private val centerJpaRepository: CenterJpaRepository,
) {

    fun findByBusinessRegistrationNumber(businessRegistrationNumber: BusinessRegistrationNumber): Center? {
        return centerJpaRepository.findByBusinessRegistrationNumber(businessRegistrationNumber.value)
    }

    fun create(
        officeNumber: String,
        centerName: String,
        businessRegistrationNumber: BusinessRegistrationNumber,
        roadNameAddress: String,
        lotNumberAddress: String,
        detailedAddress: String,
        longitude: String,
        latitude: String,
        introduce: String?,
    ) {
        centerJpaRepository.save(
            Center(
                id = UuidCreator.create(),
                officeNumber = officeNumber,
                centerName = centerName,
                businessRegistrationNumber = businessRegistrationNumber.value,
                roadNameAddress = roadNameAddress,
                lotNumberAddress = lotNumberAddress,
                detailedAddress = detailedAddress,
                longitude = BigDecimal(longitude),
                latitude = BigDecimal(latitude),
                introduce = introduce,
                profileImageUrl = null,
            )
        )
    }

}