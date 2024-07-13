package com.swm.idle.presentation.auth.center.api

import com.swm.idle.presentation.common.exception.ErrorResponse
import com.swm.idle.presentation.common.security.annotation.Secured
import com.swm.idle.support.mapper.auth.center.JoinRequest
import com.swm.idle.support.mapper.auth.center.LoginRequest
import com.swm.idle.support.mapper.auth.center.LoginResponse
import com.swm.idle.support.mapper.auth.center.RefreshLoginTokenResponse
import com.swm.idle.support.mapper.auth.center.RefreshTokenRequest
import com.swm.idle.support.mapper.auth.center.ValidateBusinessRegistrationNumberResponse
import com.swm.idle.support.mapper.auth.center.WithdrawRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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

    @Operation(summary = "사업자 등록번호 인증 API")
    @GetMapping("/authentication/{business-registration-number}")
    @ResponseStatus(HttpStatus.OK)
    fun validateBusinessRegistrationNumber(
        @PathVariable("business-registration-number") businessRegistrationNumber: String,
    ): ValidateBusinessRegistrationNumberResponse

    @Operation(summary = "센터 로그인 API")
    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
    ): LoginResponse

    @Secured
    @Operation(summary = "센터 로그아웃 API")
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout()

    @Operation(summary = "Refresh Login Token API")
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    fun refreshLoginToken(
        @RequestBody request: RefreshTokenRequest,
    ): RefreshLoginTokenResponse

    @Secured
    @Operation(summary = "센터 회원 탈퇴 API")
    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun withdraw(
        @RequestBody request: WithdrawRequest,
    )

    @Operation(summary = "아이디 중복 체크 API")
    @GetMapping("/validation/{identifier}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "사용 가능한 아이디",
            ),
            ApiResponse(
                responseCode = "400",
                description = "이미 존재하는 아이디가 있음",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(
                            implementation = ErrorResponse::class
                        )
                    ),
                ]
            ),
        ]
    )
    fun validateIdentifier(
        @PathVariable("identifier") identifier: String,
    )

}