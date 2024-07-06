package com.swm.idle.api.auth.center.spec

import com.swm.idle.api.auth.center.dto.JoinRequest
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

    @Operation(summary = "센터 관리자 회원가입 API")
    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    fun join(
        @RequestBody request: JoinRequest,
    )

//    @Operation(summary = "센터 로그인 API")
//    @PostMapping("/login")
//    fun login(
//        @RequestBody request: LoginRequest,
//    ): LoginResponse
//
//    @Operation(summary = "센터 로그아웃 API")
//    @PostMapping("/logout")
//    @ResponseStatus(HttpStatus.OK)
//    fun logout()
//
//    @Operation(summary = "Refresh Login Token API")
//    @PostMapping("/refresh")
//    @ResponseStatus(HttpStatus.OK)
//    fun refreshLoginToken(
//        @RequestBody request: RefreshTokenRequest,
//    ): RefreshLoginTokenResponse
//
//    @Operation(summary = "센터 회원 탈퇴 API")
//    @PostMapping("/withdraw")
//    @ResponseStatus(HttpStatus.OK)
//    fun withDraw()

}
