package com.swm.idle.presentation.auth.carer.controller

import com.swm.idle.application.user.carer.facade.CarerAuthFacadeService
import com.swm.idle.application.user.vo.UserPhoneVerificationNumber
import com.swm.idle.domain.user.common.vo.BirthYear
import com.swm.idle.domain.user.common.vo.PhoneNumber
import com.swm.idle.presentation.auth.carer.api.CarerAuthApi
import com.swm.idle.support.transfer.auth.carer.CarerJoinRequest
import com.swm.idle.support.transfer.auth.carer.CarerLoginRequest
import com.swm.idle.support.transfer.auth.carer.CarerWithdrawRequest
import com.swm.idle.support.transfer.auth.center.RefreshLoginTokenResponse
import com.swm.idle.support.transfer.auth.center.RefreshTokenRequest
import com.swm.idle.support.transfer.auth.common.LoginResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class CarerAuthController(
    private val carerAuthFacadeService: CarerAuthFacadeService,
) : CarerAuthApi {

    override fun join(request: CarerJoinRequest): LoginResponse {
        return carerAuthFacadeService.join(
            carerName = request.carerName,
            birthYear = BirthYear(request.birthYear),
            genderType = request.genderType,
            phoneNumber = PhoneNumber(request.phoneNumber),
            roadNameAddress = request.roadNameAddress,
            lotNumberAddress = request.lotNumberAddress,
        )
    }

    override fun login(request: CarerLoginRequest): LoginResponse {
        return carerAuthFacadeService.login(
            phoneNumber = PhoneNumber(request.phoneNumber),
            verificationNumber = UserPhoneVerificationNumber(request.verificationNumber),
        )
    }

    override fun logout() {
        carerAuthFacadeService.logout()
    }

    override fun withdraw(request: CarerWithdrawRequest) {
        carerAuthFacadeService.withdraw(request.reason)
    }

    override fun refreshLoginToken(request: RefreshTokenRequest): RefreshLoginTokenResponse {
        return carerAuthFacadeService.refreshLoginToken(request.refreshToken)
    }

}
