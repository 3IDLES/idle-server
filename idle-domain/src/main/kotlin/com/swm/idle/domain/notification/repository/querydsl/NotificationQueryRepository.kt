package com.swm.idle.domain.notification.repository.querydsl

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.swm.idle.domain.common.dto.NotificationQueryDto
import com.swm.idle.domain.common.enums.EntityStatus
import com.swm.idle.domain.notification.jpa.QNotification.notification
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class NotificationQueryRepository(
    private val jpaQueryFactory: JPAQueryFactory,
) {

    fun findAllByUserId(
        next: UUID?,
        limit: Long,
        userId: UUID,
    ): List<NotificationQueryDto> {
        return jpaQueryFactory
            .select(
                Projections.constructor(
                    NotificationQueryDto::class.java,
                    notification.id,
                    notification.isRead,
                    notification.title,
                    notification.body,
                    notification.notificationType,
                    notification.createdAt,
                    notification.imageUrl,
                    notification.notificationDetailsJson
                )
            )
            .from(notification)
            .where(
                notification.receiverId.eq(userId)
                    .and(next?.let { notification.id.loe(it) })
                    .and(notification.entityStatus.eq(EntityStatus.ACTIVE))
            )
            .orderBy(notification.id.desc())
            .limit(limit)
            .fetch()
    }

}
