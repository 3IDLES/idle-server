package com.swm.idle.support.transfer.user.center

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "GetCenterProfileResponse",
    description = "센터 프로필 상세 조회"
)
data class GetCenterProfileResponse(
    @Schema(description = "센터 이름")
    val centerName: String,
    @Schema(description = "대표자(담당자) 연락처")
    val officeNumber: String,
    @Schema(description = "도로명 주소")
    val roadNameAddress: String,
    @Schema(description = "지번 주소")
    val lotNumberAddress: String,
    @Schema(description = "상세 주소")
    val detailedAddress: String,
    @Schema(description = "경도")
    val longitude: String,
    @Schema(description = "위도")
    val latitude: String,
    @Schema(description = "소개글")
    val introduce: String?,
    @Schema(description = "프로필 이미지 url")
    val profileImageUrl: String?,
)
