package com.swm.idle.api.auth.center.spec

import com.swm.idle.api.auth.center.dto.JoinRequest
import com.swm.idle.api.auth.center.dto.LoginRequest
import com.swm.idle.api.auth.center.dto.LoginResponse
import com.swm.idle.api.auth.center.dto.RefreshLoginTokenResponse
import com.swm.idle.api.auth.center.dto.RefreshTokenRequest
import com.swm.idle.api.auth.center.dto.ValidateBusinessRegistrationNumberResponse
import com.swm.idle.api.common.exception.ErrorResponse
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

    @Operation(summary = "센터 회원 탈퇴 API")
    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun withDraw()

    @Operation(summary = "아이디 중복 체크 API")
    @GetMapping("/validation/{identifier}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "중복되지 않는 아이디",
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
