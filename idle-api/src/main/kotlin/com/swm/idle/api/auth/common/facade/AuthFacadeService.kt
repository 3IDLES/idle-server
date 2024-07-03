package com.swm.idle.api.auth.common.facade

import com.swm.idle.domain.user.vo.PhoneNumber
import com.swm.idle.infrastructure.sms.auth.common.SmsService
import org.springframework.stereotype.Service

@Service
class AuthFacadeService(
    private val smsService: SmsService,
) {

    fun sendVerificationMessage(phoneNumber: PhoneNumber) {
        smsService.sendVerificationMessage(
            phoneNumber = phoneNumber
        )
    }

}
