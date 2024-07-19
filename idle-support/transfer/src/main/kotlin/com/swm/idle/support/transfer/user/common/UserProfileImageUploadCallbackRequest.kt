package com.swm.idle.support.transfer.user.common

import com.swm.idle.domain.user.common.enum.ImageFileExtension
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(
    name = "UserProfileImageUploadCallbackRequest",
    description = "유저 프로필 이미지 업로드 callback Request"
)
data class UserProfileImageUploadCallbackRequest(
    @Schema(
        description = "s3 presigned url 생성 시 발급받은 이미지 ID",
        example = "0190be5f-7edb-79fd-8f4e-839563469de8",
    )
    val imageId: UUID,
    @Schema(description = "이미지 파일 확장자")
    val imageFileExtension: ImageFileExtension,
)
