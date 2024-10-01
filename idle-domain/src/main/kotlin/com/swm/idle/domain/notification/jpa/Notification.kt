package com.swm.idle.domain.notification.jpa

import com.swm.idle.domain.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "notification")
class Notification(
    isRead: Boolean,
    title: String,
    body: String,
    receiverId: UUID,
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

}
