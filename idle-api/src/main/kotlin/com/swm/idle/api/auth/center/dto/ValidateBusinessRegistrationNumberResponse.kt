package com.swm.idle.api.auth.center.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "ValidateBusinessRegistrationNumberResponse",
    description = "사업자 등록번호 조회 및 검증 API",
)
data class ValidateBusinessRegistrationNumberResponse(
    @Schema(description = "사업자 등록 번호")
    val businessRegistrationNumber: String,
    @Schema(description = "회사명")
    val companyName: String,
)

