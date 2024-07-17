package com.swm.idle.application.user.common.service.facade

import com.swm.idle.domain.user.common.enum.ImageFileExtension
import com.swm.idle.domain.user.common.enum.UserType
import com.swm.idle.infrastructure.aws.s3.service.S3ImageService
import com.swm.idle.support.common.uuid.UuidCreator
import com.swm.idle.support.transfer.user.UserProfileImageUploadUrlResponse
import org.springframework.stereotype.Service

@Service
class UserFacadeService(
    private val s3ImageService: S3ImageService,
) {

    fun getProfileImageUploadUrl(
        userType: UserType,
        imageFileExtension: ImageFileExtension,
    ): UserProfileImageUploadUrlResponse {
        val imageId = UuidCreator.create()

        val uploadUrl = s3ImageService.getImageUploadUrl(
            userType = userType,
            imageId = imageId,
            imageFileExtension = imageFileExtension,
        )

        return UserProfileImageUploadUrlResponse(
            imageId = imageId,
            extension = imageFileExtension,
            uploadUrl = uploadUrl,
        )
    }

}