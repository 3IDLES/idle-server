package com.swm.idle.application.notification.domain

import com.swm.idle.domain.common.exception.PersistenceException
import com.swm.idle.domain.notification.jpa.Notification
import com.swm.idle.domain.notification.jpa.NotificationInfo
import com.swm.idle.domain.notification.repository.NotificationJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class NotificationService(
    private val notificationJpaRepository: NotificationJpaRepository,
) {

    @Transactional
    fun create(notificationInfo: NotificationInfo): Notification {
        return notificationJpaRepository.save(
            Notification(
                isRead = false,
                title = notificationInfo.title,
                body = notificationInfo.body,
                notificationType = notificationInfo.notificationType,
                receiverId = notificationInfo.receiverId,
                imageUrl = notificationInfo.imageUrl,
                notificationDetails = notificationInfo.notificationDetails
            )
        )
    }

    fun getById(notificationId: UUID): Notification {
        return notificationJpaRepository.findByIdOrNull(notificationId)
            ?: throw PersistenceException.ResourceNotFound("Notification(=$notificationId) 를 찾을 수 없습니다.")
    }

    @Transactional
    fun read(notification: Notification) {
        notification.read()
    }

    fun countUnreadNotificationByUserId(userId: UUID): Int {
        return notificationJpaRepository.countByUserIdWithUnreadStatus(userId)
    }

}
