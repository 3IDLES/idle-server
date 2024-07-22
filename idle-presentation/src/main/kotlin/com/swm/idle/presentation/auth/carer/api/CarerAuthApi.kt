package com.swm.idle.presentation.auth.carer.api

import com.swm.idle.support.transfer.auth.carer.CarerJoinRequest
import com.swm.idle.support.transfer.auth.carer.CarerLoginRequest
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
    )

    @Operation(summary = "요양 보호사 로그인 API")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    fun login(
        @RequestBody request: CarerLoginRequest,
    ): LoginResponse


}
