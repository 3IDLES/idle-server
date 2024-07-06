package com.swm.idle.api.auth.center.controller

import com.swm.idle.api.auth.center.dto.JoinRequest
import com.swm.idle.api.auth.center.facade.CenterAuthFacadeService
import com.swm.idle.api.auth.center.spec.CenterAuthApi
import com.swm.idle.domain.center.vo.BusinessRegistrationNumber
import com.swm.idle.domain.sms.vo.PhoneNumber
import org.springframework.web.bind.annotation.RestController

@RestController
class CenterAuthController(
    private val centerAuthFacadeService: CenterAuthFacadeService,
) : CenterAuthApi {

    override fun join(request: JoinRequest) {
        centerAuthFacadeService.join(
            identifier = request.identifier,
            password = request.password,
            phoneNumber = PhoneNumber(request.phoneNumber),
            managerName = request.managerName,
            centerBusinessRegistrationNumber = BusinessRegistrationNumber(request.centerBusinessRegistrationNumber),
        )
    }

//    override fun login(request: LoginRequest): LoginResponse {
//        TODO("Not yet implemented")
//    }
//
//    override fun logout() {
//        TODO("Not yet implemented")
//    }
//
//    override fun refreshLoginToken(request: RefreshTokenRequest): RefreshLoginTokenResponse {
//        TODO("Not yet implemented")
//    }
//
//    override fun withDraw() {
//        TODO("Not yet implemented")
//    }

}
