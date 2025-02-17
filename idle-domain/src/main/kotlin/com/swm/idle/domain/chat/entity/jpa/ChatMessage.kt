package com.swm.idle.domain.chat.entity.jpa

import com.swm.idle.domain.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "chat_message")
class ChatMessage(
    chatRoomId: UUID,
    senderId: UUID,
    receiverId: UUID,
    content: String,
) : BaseEntity() {

    @Column(updatable = false)
    var chatRoomId: UUID = chatRoomId
        private set

    @Column(updatable = false)
    var senderId: UUID = senderId
        private set

    @Column(updatable = false)
    var receiverId: UUID = receiverId
        private set

    @Column(updatable = false)
    var content: String = content
        private set

    @Column(updatable = false)
    val isRead: Boolean = false

    init {
        require(content.isNotBlank()) { "채팅 메시지는 최소 1자 이상 입력해야 합니다." }
    }
}
