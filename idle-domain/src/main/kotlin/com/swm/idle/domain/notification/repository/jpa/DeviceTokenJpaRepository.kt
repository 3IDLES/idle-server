package com.swm.idle.domain.notification.repository.jpa

import com.swm.idle.domain.notification.jpa.DeviceToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DeviceTokenJpaRepository : JpaRepository<DeviceToken, UUID> {

    fun findByDeviceToken(deviceToken: String): DeviceToken?

    fun deleteByDeviceToken(deviceToken: String)

    fun findByUserId(userId: UUID): DeviceToken?

    fun findAllByUserId(userId: UUID): List<DeviceToken>?

}
