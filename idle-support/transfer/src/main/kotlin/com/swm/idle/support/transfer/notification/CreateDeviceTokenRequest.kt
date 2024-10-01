package com.swm.idle.support.transfer.notification

import com.swm.idle.domain.user.common.enum.UserType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "CreateDeviceTokenRequest",
    description = "FCM 토큰 저장 요청",
)
data class CreateDeviceTokenRequest(
    @Schema(description = "fcm device token")
    val deviceToken: String,

    @Schema(description = "user type")
    val userType: UserType,
)
