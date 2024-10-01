package com.swm.idle.support.transfer.notification

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    name = "DeleteDeviceTokenRequest",
    description = "FCM 토큰 삭제 요청",
)
data class DeleteDeviceTokenRequest(
    @Schema(description = "fcm device token")
    val deviceToken: String,
)
