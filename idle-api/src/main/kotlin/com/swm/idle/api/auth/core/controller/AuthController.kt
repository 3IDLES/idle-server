package com.swm.idle.api.auth.core.controller

import com.swm.idle.api.auth.core.dto.ConfirmSmsVerificationRequest
import com.swm.idle.api.auth.core.dto.SendSmsVerificationRequest
import com.swm.idle.api.auth.core.facade.AuthFacadeService
import com.swm.idle.api.auth.core.spec.AuthApi
import com.swm.idle.domain.user.vo.PhoneNumber
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    val authFacadeService: AuthFacadeService,
) : AuthApi {

    override fun sendAuthenticationMessage(request: SendSmsVerificationRequest) {
        authFacadeService.sendVerificationMessage(
            PhoneNumber(request.phoneNumber)
        )
    }

    override fun verifyAuthenticationMessage(request: ConfirmSmsVerificationRequest) {
        TODO("Not yet implemented")
    }

}
