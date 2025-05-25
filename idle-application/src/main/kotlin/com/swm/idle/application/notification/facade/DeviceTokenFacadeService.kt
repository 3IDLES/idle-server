package com.swm.idle.application.notification.facade

import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.notification.domain.DeviceTokenService
import com.swm.idle.domain.user.common.enum.UserType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class DeviceTokenFacadeService(
    private val deviceTokenService: DeviceTokenService,
) {

    @Transactional
    fun createDeviceToken(deviceToken: String, userType: UserType) {
        val userId = getUserAuthentication().userId

        val existingTokenByDevice = deviceTokenService.findByDeviceToken(deviceToken)
        val existingTokenByUser = deviceTokenService.findByUserId(userId)

        if (existingTokenByDevice != null) {
            deviceTokenService.deleteByDeviceToken(deviceToken)
        }

        if (existingTokenByUser == null) {
            deviceTokenService.save(userId, deviceToken, userType)
        } else {
            deviceTokenService.updateDeviceTokenUserId(existingTokenByUser, deviceToken)
        }
    }


    @Transactional
    fun deleteDeviceToken(deviceToken: String) {
        deviceTokenService.deleteByDeviceToken(deviceToken)
    }

}
