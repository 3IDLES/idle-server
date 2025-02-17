package com.swm.idle.domain.chat.entity.jpa

import com.swm.idle.domain.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "chat_room")
class ChatRoom(
    carerId: UUID,
    centerId: UUID,
) : BaseEntity() {

    @Column(nullable = false, updatable = false)
    var carerId: UUID = carerId
        private set

    @Column(nullable = false, updatable = false)
    var centerId: UUID = centerId
        private set
}
