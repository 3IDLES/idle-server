package com.swm.idle.presentation.user.common.api

import com.swm.idle.domain.user.common.enum.ImageFileExtension
import com.swm.idle.presentation.common.security.annotation.Secured
import com.swm.idle.support.transfer.user.common.UserProfileImageUploadCallbackRequest
import com.swm.idle.support.transfer.user.common.UserProfileImageUploadUrlResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@Tag(name = "User", description = "Users - 공통 API")
@RequestMapping("/api/v1/users", produces = ["application/json"])
interface UserApi {

    @Secured
    @Operation(summary = "프로필 이미지 업로드 URL 조회")
    @GetMapping("/{user-type}/my/profile-image/upload-url")
    @ResponseStatus(HttpStatus.OK)
    fun getProfileImageUploadUrl(
        @PathVariable("user-type")
        @Schema(description = "user type", example = "carer | center")
        userType: String,
        @Parameter(required = true)
        imageFileExtension: ImageFileExtension,
    ): UserProfileImageUploadUrlResponse

    @Secured
    @Operation(summary = "프로필 이미지 업로드 callback")
    @PostMapping("/{user-type}/my/profile-image/upload-callback")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun profileImageUploadCallback(
        @PathVariable("user-type")
        @Schema(description = "user type", example = "carer | center")
        userType: String,
        @RequestBody
        request: UserProfileImageUploadCallbackRequest,
    )

}