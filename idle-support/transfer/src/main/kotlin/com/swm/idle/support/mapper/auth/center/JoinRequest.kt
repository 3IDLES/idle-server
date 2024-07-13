package com.swm.idle.support.mapper.auth.center

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "Center Manager Join Request",
    description = "센터 관리자 회원 가입 요청"
)
data class JoinRequest(
    @Schema(description = "아이디(ID)")
    val identifier: String,
    @Schema(description = "비밀번호")
    val password: String,
    @Schema(description = "관리자 휴대폰 번호", example = "010-0000-0000")
    val phoneNumber: String,
    @Schema(description = "관리자 성명")
    val managerName: String,
    @Schema(description = "센터 사업자 등록 번호", example = "000-00-00000")
    val centerBusinessRegistrationNumber: String,
)
