package com.swm.idle.domain.notification.jpa

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.swm.idle.domain.common.entity.BaseEntity
import com.swm.idle.domain.notification.enums.NotificationType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "notification")
class Notification(
    isRead: Boolean,
    title: String,
    body: String,
    receiverId: UUID,
    imageUrl: String?,
    notificationType: NotificationType,
    notificationDetails: Map<String, Any>?,
) : BaseEntity() {

    @Column(columnDefinition = "varchar(255)")
    var isRead: Boolean = isRead
        private set

    @Column(columnDefinition = "varchar(255)")
    var title: String = title
        private set

    @Column(columnDefinition = "varchar(255)")
    var body: String = body
        private set

    var receiverId: UUID = receiverId
        private set

    @Enumerated(EnumType.STRING)
    var notificationType: NotificationType = notificationType
        private set

    @Column(columnDefinition = "varchar(255)")
    var imageUrl: String? = imageUrl
        private set

    @Column(columnDefinition = "json")
    var notificationDetailsJson: String? = notificationDetails?.let { convertMapToJson(it) }
        private set

    fun getNotificationDetails(): Map<String, Any> {
        val objectMapper = jacksonObjectMapper()
        return objectMapper.readValue(notificationDetailsJson.toString())
    }

    private fun convertMapToJson(details: Map<String, Any>): String {
        val objectMapper = jacksonObjectMapper()
        return objectMapper.writeValueAsString(details)
    }

    fun read() {
        this.isRead = true
    }

}
