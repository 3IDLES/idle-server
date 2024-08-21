package com.swm.idle.presentation.auth.common.api

import com.swm.idle.presentation.common.exception.ErrorResponse
import com.swm.idle.support.transfer.auth.center.RefreshLoginTokenResponse
import com.swm.idle.support.transfer.auth.center.RefreshTokenRequest
import com.swm.idle.support.transfer.auth.common.ConfirmSmsVerificationRequest
import com.swm.idle.support.transfer.auth.common.SendSmsVerificationRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@Tag(name = "Auth-Common", description = "공통 Auth API")
@RequestMapping("/api/v1/auth/common", produces = ["application/json"])
interface AuthApi {

    @Operation(summary = "인증번호 메세지 발송 API")
    @PostMapping("/send")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "전송 성공",
            ),
            ApiResponse(
                responseCode = "400",
                description = "전송 실패",
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
    fun sendVerificationMessage(
        @RequestBody request: SendSmsVerificationRequest,
    )

    @Operation(summary = "인증번호 메세지 검증 API")
    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "204",
                description = "인증번호 검증 성공",
            ),
            ApiResponse(
                responseCode = "400",
                description = "인증번호 검증 실패 / 인증번호가 존재하지 않음",
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
    fun confirmVerificationMessage(
        @RequestBody request: ConfirmSmsVerificationRequest,
    )

    @Operation(summary = "Refresh Login Token API")
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    fun refreshLoginToken(
        @RequestBody request: RefreshTokenRequest,
    ): RefreshLoginTokenResponse

}
