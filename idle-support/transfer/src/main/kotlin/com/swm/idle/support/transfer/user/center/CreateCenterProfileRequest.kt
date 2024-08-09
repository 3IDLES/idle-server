package com.swm.idle.support.transfer.user.center

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "CreateCenterProfileRequest",
    description = "센터 프로필 등록 요청"
)
data class CreateCenterProfileRequest(
    @Schema(description = "담당자 연락처")
    val officeNumber: String,

    @Schema(description = "센터명")
    val centerName: String,

    @Schema(description = "도로명 주소")
    val roadNameAddress: String,

    @Schema(description = "지번 주소")
    val lotNumberAddress: String,

    @Schema(description = "상세 주소")
    val detailedAddress: String,

    @Schema(description = "소개")
    val introduce: String? = null,
)
