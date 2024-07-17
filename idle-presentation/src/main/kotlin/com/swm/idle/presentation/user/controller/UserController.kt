package com.swm.idle.presentation.user.controller

import com.swm.idle.application.user.common.service.facade.UserFacadeService
import com.swm.idle.domain.user.common.enum.ImageFileExtension
import com.swm.idle.domain.user.common.enum.UserType
import com.swm.idle.presentation.user.api.UserApi
import com.swm.idle.support.transfer.user.UserProfileImageUploadUrlResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userFacadeService: UserFacadeService,
) : UserApi {

    override fun getProfileImageUploadUrl(
        userType: UserType,
        imageFileExtension: ImageFileExtension,
    ): UserProfileImageUploadUrlResponse {
        return userFacadeService.getProfileImageUploadUrl(
            userType = userType,
            imageFileExtension = imageFileExtension,
        )
    }

}