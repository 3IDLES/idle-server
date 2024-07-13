package com.swm.idle.presentation.auth.center.controller

import com.swm.idle.application.user.center.service.facade.CenterAuthFacadeService
import com.swm.idle.domain.user.center.vo.BusinessRegistrationNumber
import com.swm.idle.domain.user.center.vo.Identifier
import com.swm.idle.domain.user.center.vo.Password
import com.swm.idle.domain.user.common.vo.PhoneNumber
import com.swm.idle.presentation.auth.center.api.CenterAuthApi
import com.swm.idle.support.mapper.auth.center.JoinRequest
import com.swm.idle.support.mapper.auth.center.LoginRequest
import com.swm.idle.support.mapper.auth.center.LoginResponse
import com.swm.idle.support.mapper.auth.center.RefreshLoginTokenResponse
import com.swm.idle.support.mapper.auth.center.RefreshTokenRequest
import com.swm.idle.support.mapper.auth.center.ValidateBusinessRegistrationNumberResponse
import com.swm.idle.support.mapper.auth.center.WithdrawRequest
import org.springframework.web.bind.annotation.RestController

@RestController
class CenterAuthController(
    private val centerAuthFacadeService: CenterAuthFacadeService,
) : CenterAuthApi {

    override fun join(request: JoinRequest) {
        centerAuthFacadeService.join(
            identifier = Identifier(request.identifier),
            password = Password(request.password),
            phoneNumber = PhoneNumber(request.phoneNumber),
            managerName = request.managerName,
            centerBusinessRegistrationNumber = BusinessRegistrationNumber(request.centerBusinessRegistrationNumber),
        )
    }

    override fun validateBusinessRegistrationNumber(businessRegistrationNumber: String): ValidateBusinessRegistrationNumberResponse {
        return centerAuthFacadeService.validateCompany(
            businessRegistrationNumber = BusinessRegistrationNumber(businessRegistrationNumber),
        )
    }

    override fun login(request: LoginRequest): LoginResponse {
        return centerAuthFacadeService.login(
            identifier = Identifier(request.identifier),
            password = Password(request.password),
        )
    }

    override fun validateIdentifier(identifier: String) {
        centerAuthFacadeService.validateIdentifier(Identifier(identifier))
    }

    override fun logout() {
        centerAuthFacadeService.logout()
    }

    override fun refreshLoginToken(request: RefreshTokenRequest): RefreshLoginTokenResponse {
        return centerAuthFacadeService.refreshLoginToken(request.refreshToken)
    }

    override fun withdraw(request: WithdrawRequest) {
        return centerAuthFacadeService.withDraw(
            reason = request.reason,
            password = Password(request.password)
        )
    }

}
