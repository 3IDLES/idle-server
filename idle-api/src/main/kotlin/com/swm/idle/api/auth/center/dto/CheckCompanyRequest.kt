package com.swm.idle.api.auth.center.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Check Business Registration Number Request",
    description = "센터 사업자 등록번호 조회 및 검증 요청"
)
data class CheckCompanyRequest(
    @Schema(description = "사업자 등록 번호")
    val businessRegistrationNumber: String,
)

