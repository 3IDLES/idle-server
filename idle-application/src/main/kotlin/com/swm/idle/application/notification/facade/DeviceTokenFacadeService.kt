package com.swm.idle.application.notification.facade

import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.notification.domain.DeviceTokenService
import com.swm.idle.domain.user.common.enum.UserType
import org.springframework.stereotype.Service

@Service
class DeviceTokenFacadeService(
    private val deviceTokenService: DeviceTokenService,
) {

    fun createDeviceToken(deviceToken: String, userType: UserType) {
        val userId = getUserAuthentication().userId
        deviceTokenService.findByDeviceToken(deviceToken)?.let {
            if (it.userId != userId) {
                deviceTokenService.updateDeviceTokenUserId(it, userId)
            }
        } ?: deviceTokenService.save(
            userId = userId,
            deviceToken = deviceToken,
            userType = userType,
        )
    }

    fun deleteDeviceToken(deviceToken: String) {
        deviceTokenService.deleteByDeviceToken(deviceToken)
    }

}
