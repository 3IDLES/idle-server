package com.swm.idle.support.transfer.user.common

import com.swm.idle.domain.user.common.enum.ImageFileExtension
import io.swagger.v3.oas.annotations.media.Schema
import java.net.URL
import java.util.*

@Schema(
    name = "UserProfileImageUploadUrlResponse",
    description = "유저 프로필 이미지 업로드 Url 응답"
)
data class UserProfileImageUploadUrlResponse(
    @Schema(description = "이미지 ID", example = "8bdf126a-2d71-4a1a-9d90-6b5e5db4e0c8")
    val imageId: UUID,
    @Schema(description = "이미지 파일 확장자", example = "jpg")
    val imageFileExtension: ImageFileExtension,
    @Schema(description = "이미지 업로드 Url")
    val uploadUrl: URL,
)