package com.swm.idle.support.transfer.user.carer

import com.swm.idle.domain.user.carer.enums.JobSearchStatus
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "UpdateMyCarerProfileRequest",
    description = "요양 보호사 본인 프로필 수정 요청"
)
data class UpdateMyCarerProfileRequest(
    @Schema(description = "연차")
    val experienceYear: Int?,
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
    @Schema(description = "현재 구인 상태")
    val jobSearchStatus: JobSearchStatus,
)
