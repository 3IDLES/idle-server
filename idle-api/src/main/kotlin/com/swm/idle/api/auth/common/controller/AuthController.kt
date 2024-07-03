package com.swm.idle.api.auth.common.controller

import com.swm.idle.api.auth.common.dto.ConfirmVerificationMessageRequest
import com.swm.idle.api.auth.common.dto.SendVerificationMessageRequest
import com.swm.idle.api.auth.common.facade.AuthFacadeService
import com.swm.idle.api.auth.common.spec.AuthApi
import com.swm.idle.domain.user.vo.PhoneNumber
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    val authFacadeService: AuthFacadeService,
) : AuthApi {

    override fun sendAuthenticationMessage(request: SendVerificationMessageRequest) {
        authFacadeService.sendVerificationMessage(
            PhoneNumber(request.phoneNumber)
        )
    }

    override fun verifyAuthenticationMessage(request: ConfirmVerificationMessageRequest) {
        TODO("Not yet implemented")
    }

}
