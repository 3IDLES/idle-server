package com.swm.idle.presentation.user.common.controller

import com.swm.idle.application.user.common.service.facade.UserFacadeService
import com.swm.idle.domain.user.common.enum.ImageFileExtension
import com.swm.idle.domain.user.common.enum.UserType
import com.swm.idle.presentation.user.common.api.UserApi
import com.swm.idle.support.transfer.user.common.UserProfileImageUploadCallbackRequest
import com.swm.idle.support.transfer.user.common.UserProfileImageUploadUrlResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userFacadeService: UserFacadeService,
) : UserApi {

    override fun getProfileImageUploadUrl(
        userType: String,
        imageFileExtension: ImageFileExtension,
    ): UserProfileImageUploadUrlResponse {
        return userFacadeService.getProfileImageUploadUrl(
            userType = UserType.from(userType),
            imageFileExtension = imageFileExtension,
        )
    }

    override fun profileImageUploadCallback(
        userType: String,
        request: UserProfileImageUploadCallbackRequest,
    ) {
        return userFacadeService.createImageUploadCallback(
            userType = UserType.from(userType),
            imageId = request.imageId,
            imageFileExtension = request.imageFileExtension,
        )
    }

}