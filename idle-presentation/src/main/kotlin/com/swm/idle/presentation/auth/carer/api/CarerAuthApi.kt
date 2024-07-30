package com.swm.idle.presentation.auth.carer.api

import com.swm.idle.presentation.common.security.annotation.Secured
import com.swm.idle.support.transfer.auth.carer.CarerJoinRequest
import com.swm.idle.support.transfer.auth.carer.CarerLoginRequest
import com.swm.idle.support.transfer.auth.carer.CarerWithdrawRequest
import com.swm.idle.support.transfer.auth.center.RefreshLoginTokenResponse
import com.swm.idle.support.transfer.auth.center.RefreshTokenRequest
import com.swm.idle.support.transfer.auth.common.LoginResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@Tag(name = "Auth-Carer", description = "Carer Auth API")
@RequestMapping("/api/v1/auth/carer", produces = ["application/json"])
interface CarerAuthApi {

    @Operation(summary = "요양 보호사 회원가입 API")
    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    fun join(
        @RequestBody request: CarerJoinRequest,
    ): LoginResponse

    @Operation(summary = "요양 보호사 로그인 API")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    fun login(
        @RequestBody request: CarerLoginRequest,
    ): LoginResponse

    @Secured
    @Operation(summary = "요양 보호사 로그아웃 API")
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout()

    @Secured
    @Operation(summary = "요양 보호사 회원 탈퇴 API")
    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun withdraw(
        @RequestBody request: CarerWithdrawRequest,
    )

    @Operation(summary = "요양 보호사 Refresh Login Token API")
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    fun refreshLoginToken(
        @RequestBody request: RefreshTokenRequest,
    ): RefreshLoginTokenResponse

}
