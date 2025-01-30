package com.swm.idle.presentation.batch.api

import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@Tag(name = "Applys - Batch", description = "배치 API")
@RequestMapping("/api/v2/batch", produces = ["application/json;charset=utf-8"])
interface BatchApi {

    @Hidden
    @Operation(summary = "배치 엔트포인트 실행 API")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun launchBatch()
}