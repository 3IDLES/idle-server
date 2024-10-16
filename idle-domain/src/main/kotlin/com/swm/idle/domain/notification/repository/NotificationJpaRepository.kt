package com.swm.idle.domain.notification.repository

import com.swm.idle.domain.notification.jpa.Notification
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface NotificationJpaRepository : JpaRepository<Notification, UUID> {
}
