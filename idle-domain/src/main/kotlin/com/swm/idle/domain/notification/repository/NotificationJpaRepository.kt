package com.swm.idle.domain.notification.repository

import com.swm.idle.domain.notification.jpa.Notification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface NotificationJpaRepository : JpaRepository<Notification, UUID> {

    @Query(
        """
            SELECT COUNT(*) 
            FROM notification
            WHERE notification.receiver_id = :userId
            AND notification.is_read = false
        """,
        nativeQuery = true
    )
    fun countByUserIdWithUnreadStatus(userId: UUID): Int
}
