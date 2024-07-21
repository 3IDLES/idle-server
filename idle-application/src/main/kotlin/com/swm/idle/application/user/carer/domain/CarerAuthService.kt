package com.swm.idle.application.user.carer.domain

import com.swm.idle.domain.user.carer.entity.Carer
import com.swm.idle.domain.user.carer.repository.jpa.CarerJpaRepository
import com.swm.idle.domain.user.common.enum.GenderType
import com.swm.idle.domain.user.common.vo.BirthYear
import com.swm.idle.domain.user.common.vo.PhoneNumber
import com.swm.idle.support.common.uuid.UuidCreator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class CarerAuthService(
    private val carerJpaRepository: CarerJpaRepository,
) {

    @Transactional
    fun create(
        carerName: String,
        birthYear: BirthYear,
        gender: GenderType,
        phoneNumber: PhoneNumber,
        roadNameAddress: String,
        lotNumberAddress: String,
        longitude: String,
        latitude: String,
    ) {
        carerJpaRepository.save(
            Carer(
                id = UuidCreator.create(),
                birthYear = birthYear.value,
                carerName = carerName,
                gender = gender,
                phoneNumber = phoneNumber.value,
                roadNameAddress = roadNameAddress,
                lotNumberAddress = lotNumberAddress,
                longitude = BigDecimal(longitude),
                latitude = BigDecimal(latitude),
            )
        )
    }

    fun findByPhoneNumber(phoneNumber: PhoneNumber): Carer? {
        return carerJpaRepository.findByPhoneNumber(phoneNumber.value)
    }

}
