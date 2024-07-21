package com.swm.idle.application.user.carer.facade

import com.swm.idle.application.user.carer.domain.CarerAuthService
import com.swm.idle.domain.user.carer.exception.CarerException
import com.swm.idle.domain.user.common.enum.GenderType
import com.swm.idle.domain.user.common.vo.BirthYear
import com.swm.idle.domain.user.common.vo.PhoneNumber
import org.springframework.stereotype.Service

@Service
class CarerAuthFacadeService(
    private val carerAuthService: CarerAuthService,
) {

    fun join(
        carerName: String,
        birthYear: BirthYear,
        genderType: GenderType,
        phoneNumber: PhoneNumber,
        roadNameAddress: String,
        lotNumberAddress: String,
        longitude: String,
        latitude: String,
    ) {
        carerAuthService.findByPhoneNumber(phoneNumber)?.let {
            throw CarerException.AlreadyExistCarer()
        }

        carerAuthService.create(
            carerName = carerName,
            birthYear = birthYear,
            gender = genderType,
            phoneNumber = phoneNumber,
            roadNameAddress = roadNameAddress,
            lotNumberAddress = lotNumberAddress,
            longitude = longitude,
            latitude = latitude,
        )
    }

}
