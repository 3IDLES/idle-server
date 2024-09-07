package com.swm.idle.presentation.user.carer.api

import com.swm.idle.presentation.common.security.annotation.Secured
import com.swm.idle.support.transfer.user.common.logging.GetMyCarerInfoResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@Tag(name = "[Logging] User-Carer", description = "[Logging] Users - 요양 보호사 API")
@RequestMapping("/api/v1/logs/users/carer", produces = ["application/json;charset=UTF-8"])
interface CarerLoggingApi {

    @Secured
    @Operation(summary = "로그인 된 요양 보호사 정보 조회 API")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getMyCarerInfo(): GetMyCarerInfoResponse

}
