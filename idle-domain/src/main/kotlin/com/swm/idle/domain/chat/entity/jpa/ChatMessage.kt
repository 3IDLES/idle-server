package com.swm.idle.domain.chat.entity.jpa

import com.swm.idle.domain.chat.enums.ContentType
import com.swm.idle.domain.chat.enums.SenderType
//import com.swm.idle.domain.chat.vo.Content
import com.swm.idle.domain.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.util.*

@Entity
@Table(name = "chat_message")
class ChatMessage(
    roomId: UUID,
    senderId: UUID,
    senderType: SenderType,
    contents: List<Content>,
) : BaseEntity() {

    @Column(updatable = false)
    var roomId: UUID = roomId
        private set

    @Column(updatable = false)
    var senderId: UUID = senderId
        private set

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", updatable = false)
    var senderType: SenderType = senderType
        private set

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "contents", nullable = false, columnDefinition = "json")
    var contents: List<Content> = contents
        private set

    data class Content(
        val type: ContentType,
        val value: String,
    )

}
