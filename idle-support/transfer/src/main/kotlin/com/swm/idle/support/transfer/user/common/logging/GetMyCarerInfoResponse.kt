package com.swm.idle.support.transfer.user.common.logging

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(
    name = "GetMyCarerInfoResponse",
    description = "로그인된 요양 보호사 정보 조회 응답"
)
data class GetMyCarerInfoResponse(
    @Schema(description = "요양 보호사 ID")
    val carerId: UUID,
)
