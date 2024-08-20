package com.swm.idle.presentation.applys.api

import com.swm.idle.presentation.common.security.annotation.Secured
import com.swm.idle.support.transfer.applys.CreateApplyRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@Tag(name = "Applyment - Carer", description = "요양 보호사 공고 지원 API")
@RequestMapping("/api/v1/applyments", produces = ["application/json;charset=utf-8"])
interface CarerApplyApi {

    @Secured
    @Operation(summary = "요양 보호사 공고 지원하기 API")
    @PostMapping("/history")
    @ResponseStatus(HttpStatus.OK)
    fun createApply(
        @RequestBody request: CreateApplyRequest,
    )

}
