package com.swm.idle.application.user.common.service.facade

import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.user.center.service.domain.CenterManagerService
import com.swm.idle.application.user.center.service.domain.CenterService
import com.swm.idle.domain.user.center.exception.CenterException
import com.swm.idle.domain.user.center.vo.BusinessRegistrationNumber
import com.swm.idle.domain.user.common.enum.ImageFileExtension
import com.swm.idle.domain.user.common.enum.UserType
import com.swm.idle.domain.user.common.exception.UserException
import com.swm.idle.infrastructure.aws.s3.service.S3ImageService
import com.swm.idle.support.common.uuid.UuidCreator
import com.swm.idle.support.transfer.user.common.UserProfileImageUploadUrlResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserFacadeService(
    private val s3ImageService: S3ImageService,
    private val centerManagerService: CenterManagerService,
    private val centerService: CenterService,
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
            imageFileExtension = imageFileExtension,
            uploadUrl = uploadUrl,
        )
    }

    @Transactional
    fun createImageUploadCallback(
        userType: UserType,
        imageId: UUID,
        imageFileExtension: ImageFileExtension,
    ) {
        try {
            s3ImageService.findByIdAndExtension(
                userType = userType,
                imageId = imageId,
                imageFileExtension = imageFileExtension,
            )
        } catch (e: Exception) {
            UserException.ImageUploadNotCompleted()
        }

        if (userType == UserType.CENTER) {
            updateCenterImageURL(
                userType = userType,
                imageId = imageId,
                imageFileExtension = imageFileExtension,
            )
        }

        // TODO("요양 보호사 프로필 업로드 로직 구현")
    }

    fun updateCenterImageURL(
        userType: UserType,
        imageId: UUID,
        imageFileExtension: ImageFileExtension,
    ) {
        val centerManager = getUserAuthentication().userId
            .let { centerManagerService.getById(it) }

        centerService.findByBusinessRegistrationNumber(
            BusinessRegistrationNumber(centerManager.centerBusinessRegistrationNumber)
        )?.also {
            val profileImageUrl = s3ImageService.findByIdAndExtension(
                userType = userType,
                imageId = imageId,
                imageFileExtension = imageFileExtension,
            )
            it.updateProfileImageUrl(profileImageUrl.toString())
        } ?: throw CenterException.NotFoundException()
    }

}