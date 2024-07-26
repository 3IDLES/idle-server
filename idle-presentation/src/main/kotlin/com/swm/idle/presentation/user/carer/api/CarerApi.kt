package com.swm.idle.presentation.user.carer.api

import com.swm.idle.presentation.common.security.annotation.Secured
import com.swm.idle.support.transfer.user.carer.GetCarerProfileResponse
import com.swm.idle.support.transfer.user.carer.UpdateMyCarerProfileRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@Tag(name = "User-Carer", description = "Users - 요양 보호사 API")
@RequestMapping("/api/v1/users/carer", produces = ["application/json;charset=UTF-8"])
interface CarerApi {

    @Secured
    @Operation(summary = "요양 보호사 본인 프로필 조회 API")
    @GetMapping("/my/profile")
    @ResponseStatus(HttpStatus.OK)
    fun getMyCarerProfile(): GetCarerProfileResponse

    @Secured
    @Operation(summary = "요양 보호사 프로필 조회 API")
    @GetMapping("/profile/{carer-id}")
    @ResponseStatus(HttpStatus.OK)
    fun getCarerProfile(@PathVariable("carer-id") carerId: UUID): GetCarerProfileResponse

    @Secured
    @Operation(summary = "요양 보호사 본인 프로필 수정 API")
    @PatchMapping("/my/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateMyCarerProfile(@RequestBody request: UpdateMyCarerProfileRequest)

}
