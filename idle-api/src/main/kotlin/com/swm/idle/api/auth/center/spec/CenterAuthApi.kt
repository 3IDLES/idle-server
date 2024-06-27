package com.swm.idle.api.auth.center.spec

import com.swm.idle.api.auth.center.dto.ConfirmVerificationMessageRequest
import com.swm.idle.api.auth.center.dto.LoginRequest
import com.swm.idle.api.auth.center.dto.LoginResponse
import com.swm.idle.api.auth.center.dto.RefreshLoginTokenResponse
import com.swm.idle.api.auth.center.dto.RefreshTokenRequest
import com.swm.idle.api.auth.center.dto.SendVerificationMessageRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@Tag(name = "Auth-Center", description = "Center Auth API")
@RequestMapping("/api/v1/auth/center", produces = ["application/json"])
interface CenterAuthApi {

    @Operation(summary = "인증번호 메세지 전송 API")
    @PostMapping("/send")
    fun sendAuthenticationMessage(
        @RequestBody request: SendVerificationMessageRequest,
    )

    @Operation(summary = "인증번호 메세지 검증 API")
    @PostMapping("/confirm")
    fun verifyAuthenticationMessage(
        @RequestBody request: ConfirmVerificationMessageRequest,
    )

    @Operation(summary = "센터 로그인 API")
    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
    ): LoginResponse

    @Operation(summary = "센터 로그아웃 API")
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    fun logout()

    @Operation(summary = "Refresh Login Token")
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    fun refreshLoginToken(
        @RequestBody request: RefreshTokenRequest,
    ): RefreshLoginTokenResponse

    @Operation(summary = "Withdraw Account ")
    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.OK)
    fun withDraw()

}
