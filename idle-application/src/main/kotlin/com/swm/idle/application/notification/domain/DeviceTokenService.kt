package com.swm.idle.application.notification.domain

import com.swm.idle.domain.notification.jpa.DeviceToken
import com.swm.idle.domain.notification.repository.jpa.DeviceTokenJpaRepository
import com.swm.idle.domain.user.common.enum.UserType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class DeviceTokenService(
    private val deviceTokenJpaRepository: DeviceTokenJpaRepository,
) {

    fun findByDeviceToken(deviceToken: String): DeviceToken? {
        return deviceTokenJpaRepository.findByDeviceToken(deviceToken)
    }

    fun findAllByUserId(userId: UUID): List<DeviceToken>? {
        return deviceTokenJpaRepository.findAllByUserId(userId)
    }

    @Transactional
    fun updateDeviceTokenUserId(
        deviceToken: DeviceToken,
        userId: UUID,
    ) {
        deviceToken.updateUserId(userId)
    }

    @Transactional
    fun deleteByDeviceToken(deviceToken: String) {
        deviceTokenJpaRepository.deleteByDeviceToken(deviceToken)
    }

    @Transactional
    fun save(
        userId: UUID,
        deviceToken: String,
        userType: UserType,
    ) {
        DeviceToken(
            userId = userId,
            deviceToken = deviceToken,
            userType = userType,
        ).also {
            deviceTokenJpaRepository.save(it)
        }
    }

}
