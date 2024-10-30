package com.swm.idle.application.notification.facade

import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.notification.domain.NotificationService
import com.swm.idle.application.notification.utils.converter.NotificationDetailsConverter
import com.swm.idle.domain.common.dto.NotificationQueryDto
import com.swm.idle.support.security.exception.SecurityException
import com.swm.idle.support.transfer.common.CursorScrollRequest
import com.swm.idle.support.transfer.notification.NotificationScrollResponse
import com.swm.idle.support.transfer.notification.UnreadNotificationCountResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class NotificationFacadeService(
    private val notificationService: NotificationService,
    private val notificationDetailsConverter: NotificationDetailsConverter,
) {

    @Transactional
    fun readNotification(notificationId: UUID) {
        val notification = notificationService.getById(notificationId)

        if (notification.receiverId != getUserAuthentication().userId) {
            throw SecurityException.UnAuthorizedRequest()
        }

        notificationService.getById(notificationId).also {
            notificationService.read(it)
        }
    }

    fun countUnreadNotification(): UnreadNotificationCountResponse {
        val userId = getUserAuthentication().userId

        return notificationService.countUnreadNotificationByUserId(userId).let {
            UnreadNotificationCountResponse(
                unreadNotificationCount = it
            )
        }
    }

    fun getNotifications(request: CursorScrollRequest): NotificationScrollResponse {
        val userId = getUserAuthentication().userId

        val (items, next) = scrollByUser(
            userId = userId,
            next = request.next,
            limit = request.limit,
        )

        return NotificationScrollResponse.from(
            items = items.map { notificationQueryDto ->
                NotificationScrollResponse.NotificationDto(
                    id = notificationQueryDto.id,
                    isRead = notificationQueryDto.isRead,
                    title = notificationQueryDto.title,
                    body = notificationQueryDto.body,
                    notificationType = notificationQueryDto.notificationType,
                    createdAt = notificationQueryDto.createdAt,
                    imageUrl = notificationQueryDto.imageUrl,
                    notificationDetails = notificationDetailsConverter.convertToMap(
                        notificationQueryDto.notificationDetails
                    )  // map으로 변환
                )
            },
            next = next,
            total = items.size
        )
    }

    private fun scrollByUser(
        userId: UUID,
        next: UUID?,
        limit: Long,
    ): Pair<List<NotificationQueryDto>, UUID?> {

        val notificationDtos = notificationService.findAllByUserId(
            next = next,
            limit = limit + 1,
            userId = userId,
        )

        val newNext =
            if (notificationDtos.size > limit) notificationDtos.last().id else null
        val items =
            if (newNext == null) notificationDtos else notificationDtos.subList(
                0,
                limit.toInt()
            )
        return items to newNext
    }

}
