package com.swm.idle.presentation.auth.common.controller

import com.swm.idle.application.user.common.service.facade.AuthFacadeService
import com.swm.idle.application.user.vo.UserPhoneVerificationNumber
import com.swm.idle.domain.user.common.vo.PhoneNumber
import com.swm.idle.presentation.auth.common.api.AuthApi
import com.swm.idle.support.mapper.auth.common.ConfirmSmsVerificationRequest
import com.swm.idle.support.mapper.auth.common.SendSmsVerificationRequest
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
            verificationNumber = UserPhoneVerificationNumber(request.verificationNumber)
        )
    }

}