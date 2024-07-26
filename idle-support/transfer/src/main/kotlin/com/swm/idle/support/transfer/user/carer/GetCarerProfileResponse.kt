package com.swm.idle.support.transfer.user.carer

import com.swm.idle.domain.user.carer.enums.JobSearchStatus
import com.swm.idle.domain.user.common.enum.GenderType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "GetCarerProfileResponse",
    description = "요양 보호사 프로필 상세 조회"
)
data class GetCarerProfileResponse(
    @Schema(description = "요양 보호사 성명")
    val carerName: String,
    @Schema(description = "나이")
    val age: Int,
    @Schema(description = "성별")
    val gender: GenderType,
    @Schema(description = "연차")
    val experienceYear: Int?,
    @Schema(description = "연락처")
    val phoneNumber: String,
    @Schema(description = "도로명 주소")
    val roadNameAddress: String,
    @Schema(description = "지번 주소")
    val lotNumberAddress: String,
    @Schema(description = "경도")
    val longitude: String,
    @Schema(description = "위도")
    val latitude: String,
    @Schema(description = "소개글")
    val introduce: String?,
    @Schema(description = "특기")
    val speciality: String?,
    @Schema(description = "프로필 이미지 url")
    val profileImageUrl: String?,
    @Schema(description = "현재 구인 여부")
    val jobSearchStatus: JobSearchStatus,
)
