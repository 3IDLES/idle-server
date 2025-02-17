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

    @Query("""
    SELECT
        cr.id  AS chatRoomId,
        cr.center_id  AS receiverId,
        (
            SELECT COUNT(*)
            FROM chat_message cm2
            WHERE cm2.chat_room_id = cr.id
              AND cm2.receiver_id = :userId
              AND cm2.is_read = false
            LIMIT 100
        ) AS unreadCount,
        (
            SELECT cm3.content
            FROM chat_message cm3
            WHERE cm3.chat_room_id = cr.id
            ORDER BY cm3.id DESC
            LIMIT 1
        ) AS lastMessage,
        (
            SELECT cm3.created_at
            FROM chat_message cm3
            WHERE cm3.chat_room_id = cr.id
            ORDER BY cm3.id DESC
            LIMIT 1
        ) AS lastMessageTime
    FROM 
        chat_room cr
    WHERE 
        cr.carer_id = :userId
""", nativeQuery = true)
    fun findCaresChatroomSummaries(@Param("userId") userId: UUID): List<ChatRoomSummaryInfoProjection>

    @Query("""
    SELECT
        cr.id  AS chatRoomId,
        cr.center_id  AS receiverId,
        (
            SELECT COUNT(*)
            FROM chat_message cm2
            WHERE cm2.chat_room_id = cr.id
              AND cm2.receiver_id = :userId
              AND cm2.is_read = false
            LIMIT 100
        ) AS unreadCount,
        (
            SELECT cm3.content
            FROM chat_message cm3
            WHERE cm3.chat_room_id = cr.id
            ORDER BY cm3.id DESC
            LIMIT 1
        ) AS lastMessage,
        (
            SELECT cm3.created_at
            FROM chat_message cm3
            WHERE cm3.chat_room_id = cr.id
            ORDER BY cm3.id DESC
            LIMIT 1
        ) AS lastMessageTime
    FROM 
        chat_room cr
    WHERE 
        cr.center_id = :userId
""", nativeQuery = true)
    fun findCentersChatroomSummaries(@Param("userId") userId: UUID): List<ChatRoomSummaryInfoProjection>
}
