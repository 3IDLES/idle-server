package com.swm.idle.domain.chat.repository

import com.swm.idle.domain.chat.entity.jpa.ChatRoom
import com.swm.idle.domain.chat.vo.ChatRoomSummaryInfoProjection
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChatRoomRepository : JpaRepository<ChatRoom, UUID> {

    fun findByCarerIdAndCenterId(carerId: UUID, centerId: UUID): ChatRoom?

    @Query("""
    SELECT 
        cr.id,
        cr.carer_id,
        cr.center_id,
        cm.content,
        cm.created_at,
        cm.sequence
    FROM chat_room cr
    JOIN LATERAL (
        SELECT cm.content, cm.created_at, cm.sequence
        FROM chat_message cm
        WHERE cr.id = cm.chat_room_id 
        ORDER BY cm.id DESC
        LIMIT 1
    ) cm
    WHERE cr.id IN :chatRoomIds
""", nativeQuery = true
    )
    fun findChatRoomsWithLastMessages(@Param("chatRoomIds") chatRoomIds: Set<UUID>): List<ChatRoomSummaryInfoProjection>
}