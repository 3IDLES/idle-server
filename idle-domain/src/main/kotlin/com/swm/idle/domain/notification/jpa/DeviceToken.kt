package com.swm.idle.domain.notification.jpa

import com.swm.idle.domain.common.entity.BaseEntity
import com.swm.idle.domain.user.common.enum.UserType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "device_token")
class DeviceToken(
    deviceToken: String,
    userId: UUID,
    userType: UserType,
) : BaseEntity() {

    @Column(columnDefinition = "varchar(255)")
    var deviceToken: String = deviceToken
        private set

    var userId: UUID = userId
        private set

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255)")
    var userType: UserType = userType
        private set

    fun updateUserId(userId: UUID) {
        this.userId = userId
    }

}
