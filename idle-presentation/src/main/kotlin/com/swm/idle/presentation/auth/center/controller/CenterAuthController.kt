package com.swm.idle.presentation.auth.center.controller

import com.swm.idle.application.user.center.service.facade.CenterAuthFacadeService
import com.swm.idle.domain.user.center.vo.BusinessRegistrationNumber
import com.swm.idle.domain.user.center.vo.Identifier
import com.swm.idle.domain.user.center.vo.Password
import com.swm.idle.domain.user.common.vo.PhoneNumber
import com.swm.idle.presentation.auth.center.api.CenterAuthApi
import com.swm.idle.support.transfer.auth.center.CenterLoginRequest
import com.swm.idle.support.transfer.auth.center.CenterManagerForPendingResponse
import com.swm.idle.support.transfer.auth.center.ChangePasswordRequest
import com.swm.idle.support.transfer.auth.center.JoinRequest
import com.swm.idle.support.transfer.auth.center.ValidateBusinessRegistrationNumberResponse
import com.swm.idle.support.transfer.auth.center.WithdrawRequest
import com.swm.idle.support.transfer.auth.common.LoginResponse
import com.swm.idle.support.transfer.user.center.JoinStatusInfoResponse
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

    override fun getJoinStatusInfo(): JoinStatusInfoResponse {
        return centerAuthFacadeService.getCenterManagerJoinStatusInfo()
    }

    override fun requestCenterManagerVerification() {
        return centerAuthFacadeService.requestCenterManagerVerification()
    }

    override fun getCenterManagerForPending(): CenterManagerForPendingResponse {
        return centerAuthFacadeService.getCenterManagerForPending()
    }

    override fun validateBusinessRegistrationNumber(businessRegistrationNumber: String): ValidateBusinessRegistrationNumberResponse {
        return centerAuthFacadeService.validateCompany(
            businessRegistrationNumber = BusinessRegistrationNumber(businessRegistrationNumber),
        )
    }

    override fun login(request: CenterLoginRequest): LoginResponse {
        return centerAuthFacadeService.login(
            identifier = Identifier(request.identifier),
            password = Password(request.password),
        )
    }

    override fun validateIdentifier(identifier: String) {
        centerAuthFacadeService.validateIdentifier(Identifier(identifier))
    }

    override fun changePassword(request: ChangePasswordRequest) {
        centerAuthFacadeService.changePassword(
            phoneNumber = PhoneNumber(request.phoneNumber),
            newPassword = Password(request.newPassword)
        )
    }

    override fun logout() {
        centerAuthFacadeService.logout()
    }

    override fun withdraw(request: WithdrawRequest) {
        return centerAuthFacadeService.withdraw(
            reason = request.reason,
            password = Password(request.password)
        )
    }

}
