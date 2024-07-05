package com.swm.idle.api.auth.core.spec

import com.swm.idle.api.auth.core.dto.ConfirmSmsVerificationRequest
import com.swm.idle.api.auth.core.dto.SendSmsVerificationRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@Tag(name = "Auth-Common", description = "공통 Auth API")
@RequestMapping("/api/v1/auth/core", produces = ["application/json"])
interface AuthApi {

    @Operation(summary = "인증번호 메세지 발송 API")
    @PostMapping("/send")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun sendVerificationMessage(
        @RequestBody request: SendSmsVerificationRequest,
    )

    @Operation(summary = "인증번호 메세지 검증 API")
    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun confirmVerificationMessage(
        @RequestBody request: ConfirmSmsVerificationRequest,
    )

}
