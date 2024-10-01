package com.swm.idle.presentation.notification.controller

import com.swm.idle.application.notification.facade.DeviceTokenFacadeService
import com.swm.idle.presentation.notification.api.DeviceTokenApi
import com.swm.idle.support.transfer.notification.CreateDeviceTokenRequest
import com.swm.idle.support.transfer.notification.DeleteDeviceTokenRequest
import org.springframework.web.bind.annotation.RestController

@RestController
class DeviceTokenController(
    private val deviceTokenFacadeService: DeviceTokenFacadeService,
) : DeviceTokenApi {

    override fun createDeviceToken(request: CreateDeviceTokenRequest) {
        deviceTokenFacadeService.createDeviceToken(
            deviceToken = request.deviceToken,
            userType = request.userType,
        )
    }

    override fun deleteDeviceToken(request: DeleteDeviceTokenRequest) {
        deviceTokenFacadeService.deleteDeviceToken(request.deviceToken)
    }

}
