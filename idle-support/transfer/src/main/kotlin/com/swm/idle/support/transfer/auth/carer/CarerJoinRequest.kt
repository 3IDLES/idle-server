package com.swm.idle.support.transfer.auth.carer

import com.swm.idle.domain.user.common.enum.GenderType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Carer Join Request",
    description = "요양 보호사 회원 가입 요청"
)
data class CarerJoinRequest(
    @Schema(description = "성명")
    val carerName: String,
    @Schema(description = "출생년도")
    val birthYear: Int,
    @Schema(description = "성별", example = "MAN, WOMAN")
    val genderType: GenderType,
    @Schema(description = "연락처", example = "010-1234-5678")
    val phoneNumber: String,
    @Schema(description = "도로명 주소")
    val roadNameAddress: String,
    @Schema(description = "지번 주소")
    val lotNumberAddress: String,
)
