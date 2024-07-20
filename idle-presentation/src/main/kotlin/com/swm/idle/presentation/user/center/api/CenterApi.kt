package com.swm.idle.presentation.user.center.api

import com.swm.idle.presentation.common.security.annotation.Secured
import com.swm.idle.support.transfer.user.center.CreateCenterProfileRequest
import com.swm.idle.support.transfer.user.center.GetMyCenterProfileResponse
import com.swm.idle.support.transfer.user.center.UpdateCenterProfileRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@Tag(name = "User-Center", description = "Users - 센터 API")
@RequestMapping("/api/v1/users/center", produces = ["application/json"])
interface CenterApi {

    @Secured
    @Operation(summary = "센터 프로필 등록 API")
    @PostMapping("/my/profile")
    @ResponseStatus(HttpStatus.CREATED)
    fun createCenterProfile(
        @RequestBody request: CreateCenterProfileRequest,
    )

    @Secured
    @Operation(summary = "센터 프로필 수정 API")
    @PatchMapping("/my/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateCenterProfile(
        @RequestBody request: UpdateCenterProfileRequest,
    )

    @Secured
    @Operation(summary = "내 센터 프로필 조회 API")
    @GetMapping("/my/profile")
    @ResponseStatus(HttpStatus.OK)
    fun getMyCenterProfile(): GetMyCenterProfileResponse

}