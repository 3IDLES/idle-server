package com.swm.idle.domain.chat.entity.jpa

import com.swm.idle.domain.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "chat_room")
class ChatRoom(
    senderId: UUID,
    receiverId: UUID,
) : BaseEntity() {

    @Column(nullable = false, updatable = false)
    var senderId: UUID = senderId
        private set

    @Column(nullable = false, updatable = false)
    var receiverId: UUID = receiverId
        private set

}
