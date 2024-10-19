package com.swm.idle.support.transfer.notification

import com.swm.idle.domain.notification.enums.NotificationType
import com.swm.idle.support.transfer.common.ScrollResponse
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import java.util.*

@Schema(
    name = "NotificationScrollResponse",
    description = "알림 전체 조회 페이징 응답",
)
data class NotificationScrollResponse(
    override val items: List<NotificationDto>,
    override val next: UUID?,
    override val total: Int,
) : ScrollResponse<NotificationScrollResponse.NotificationDto, UUID?>(
    items = items,
    next = next,
    total = total,
) {

    data class NotificationDto(
        val id: UUID,
        val isRead: Boolean,
        val title: String,
        val body: String,
        val notificationType: NotificationType,
        val createdAt: LocalDateTime,
        val imageUrl: String?,
        val notificationDetails: Map<String, Any>,
    )

    companion object {

        fun from(
            items: List<NotificationDto>,
            next: UUID?,
            total: Int,
        ): NotificationScrollResponse {
            return NotificationScrollResponse(
                items = items,
                next = next,
                total = total,
            )
        }
    }

}
