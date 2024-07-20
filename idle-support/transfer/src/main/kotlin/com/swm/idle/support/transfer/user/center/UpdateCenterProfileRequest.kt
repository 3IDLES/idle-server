package com.swm.idle.support.transfer.user.center

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "UpdateCenterProfileRequest",
    description = "센터 프로필 수정 요청"
)
data class UpdateCenterProfileRequest(
    @Schema(description = "담당자 연락처")
    val officeNumber: String,

    @Schema(description = "소개")
    val introduce: String? = null,
)