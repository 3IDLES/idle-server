package com.swm.idle.api.auth.core.controller

import com.swm.idle.api.auth.core.dto.ConfirmSmsVerificationRequest
import com.swm.idle.api.auth.core.dto.SendSmsVerificationRequest
import com.swm.idle.api.auth.core.facade.AuthFacadeService
import com.swm.idle.api.auth.core.spec.AuthApi
import com.swm.idle.domain.sms.vo.PhoneNumber
import com.swm.idle.domain.sms.vo.SmsVerificationNumber
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authFacadeService: AuthFacadeService,
) : AuthApi {

    override fun sendVerificationMessage(request: SendSmsVerificationRequest) {
        authFacadeService.sendVerificationMessage(
            phoneNumber = PhoneNumber(request.phoneNumber)
        )
    }

    override fun confirmVerificationMessage(request: ConfirmSmsVerificationRequest) {
        authFacadeService.confirmVerificationMessage(
            phoneNumber = PhoneNumber(request.phoneNumber),
            verificationNumber = SmsVerificationNumber(request.verificationNumber)
        )
    }

}
