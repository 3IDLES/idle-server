package com.swm.idle.presentation.notification.api

import com.swm.idle.presentation.common.security.annotation.Secured
import com.swm.idle.support.transfer.notification.CreateDeviceTokenRequest
import com.swm.idle.support.transfer.notification.DeleteDeviceTokenRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus

@Tag(name = "FCM Device Token", description = "FCM 디바이스 토큰 API")
@RequestMapping("/api/v1/fcm", produces = ["application/json;charset=utf-8"])
interface DeviceTokenApi {

    @Secured
    @Operation(summary = "FCM 토큰 저장 API")
    @PostMapping("/token")
    @ResponseStatus(HttpStatus.CREATED)
    fun createDeviceToken(@RequestBody request: CreateDeviceTokenRequest)

    @Secured
    @Operation(summary = "FCM 토큰 삭제 API")
    @DeleteMapping("/token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteDeviceToken(@RequestBody request: DeleteDeviceTokenRequest)

}
