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
    WITH FilteredChatRooms AS (
        SELECT
            cr.id AS chat_room_id,
            cr.center_id 
        FROM chat_room cr
        WHERE cr.carer_id = :userId
    ),
    
    UnreadMessageCounts AS (
        SELECT 
            cm.chat_room_id,
            COUNT(*) AS unread_count
        FROM chat_message cm
        WHERE cm.chat_room_id IN (SELECT chat_room_id FROM FilteredChatRooms)
          AND cm.is_read = false
        GROUP BY cm.chat_room_id
    )
    
    SELECT
        fcr.chat_room_id AS chatRoomId,
        fcr.center_id AS opponentId,
        umc.unread_count AS unreadCount,  
        cm.content AS lastMessage,
        cm.created_at AS lastMessageTime
    FROM FilteredChatRooms fcr
    JOIN UnreadMessageCounts umc ON fcr.chat_room_id = umc.chat_room_id
    JOIN LATERAL (
        SELECT content, created_at
        FROM chat_message
        WHERE chat_room_id = fcr.chat_room_id AND is_read = false
        ORDER BY id DESC
        LIMIT 1
    ) cm;
""", nativeQuery = true)
    fun findCaresChatroomSummaries(@Param("userId") userId: UUID): List<ChatRoomSummaryInfoProjection>

    @Query("""
    WITH FilteredChatRooms AS (
        SELECT
            cr.id AS chat_room_id,
            cr.center_id 
        FROM chat_room cr
        WHERE cr.center_id = :userId
    ),
    
    UnreadMessageCounts AS (
        SELECT 
            cm.chat_room_id,
            COUNT(*) AS unread_count
        FROM chat_message cm
        WHERE cm.chat_room_id IN (SELECT chat_room_id FROM FilteredChatRooms)
          AND cm.is_read = false
        GROUP BY cm.chat_room_id
    )
    
    SELECT
        fcr.chat_room_id AS chatRoomId,
        fcr.carer_id AS opponentId,
        umc.unread_count AS unreadCount,  
        cm.content AS lastMessage,
        cm.created_at AS lastMessageTime
    FROM FilteredChatRooms fcr
    JOIN UnreadMessageCounts umc ON fcr.chat_room_id = umc.chat_room_id
    JOIN LATERAL (
        SELECT content, created_at
        FROM chat_message
        WHERE chat_room_id = fcr.chat_room_id AND is_read = false
        ORDER BY id DESC
        LIMIT 1
    ) cm;
""", nativeQuery = true)
    fun findCentersChatroomSummaries(@Param("userId") userId: UUID): List<ChatRoomSummaryInfoProjection>


    @Query("""
    WITH FilteredChatRooms AS (
        SELECT
            cr.id AS chat_room_id,
            cr.center_id 
        FROM chat_room cr
        WHERE cr.carer_id = :carerId
        AND cr.center_id =:centerId
    ),
    
    UnreadMessageCounts AS (
        SELECT 
            cm.chat_room_id,
            COUNT(*) AS unread_count
        FROM chat_message cm
        WHERE cm.chat_room_id IN (SELECT chat_room_id FROM FilteredChatRooms)
          AND cm.is_read = false
        GROUP BY cm.chat_room_id
    )
    
    SELECT
        fcr.chat_room_id AS chatRoomId,
        fcr.center_id AS opponentId,
        umc.unread_count AS unreadCount,  
        cm.content AS lastMessage,
        cm.created_at AS lastMessageTime
    FROM FilteredChatRooms fcr
    JOIN UnreadMessageCounts umc ON fcr.chat_room_id = umc.chat_room_id
    JOIN LATERAL (
        SELECT content, created_at
        FROM chat_message
        WHERE chat_room_id = fcr.chat_room_id AND is_read = false
        ORDER BY id DESC
        LIMIT 1
    ) cm;
""", nativeQuery = true)
    fun carerFindByCenterIdWithCarerId(@Param("centerId") centerId: UUID,
                                       @Param("carerId") carerId: UUID): ChatRoomSummaryInfoProjection

    @Query("""
    WITH FilteredChatRooms AS (
        SELECT
            cr.id AS chat_room_id,
            cr.center_id 
        FROM chat_room cr
        WHERE cr.carer_id = :carerId
        AND cr.center_id =:centerId
    ),
    
    UnreadMessageCounts AS (
        SELECT 
            cm.chat_room_id,
            COUNT(*) AS unread_count
        FROM chat_message cm
        WHERE cm.chat_room_id IN (SELECT chat_room_id FROM FilteredChatRooms)
          AND cm.is_read = false
        GROUP BY cm.chat_room_id
    )
    
    SELECT
        fcr.chat_room_id AS chatRoomId,
        fcr.carer_id AS opponentId,
        umc.unread_count AS unreadCount,  
        cm.content AS lastMessage,
        cm.created_at AS lastMessageTime
    FROM FilteredChatRooms fcr
    JOIN UnreadMessageCounts umc ON fcr.chat_room_id = umc.chat_room_id
    JOIN LATERAL (
        SELECT content, created_at
        FROM chat_message
        WHERE chat_room_id = fcr.chat_room_id AND is_read = false
        ORDER BY id DESC
        LIMIT 1
    ) cm;
""", nativeQuery = true)
    fun centerFindByCenterIdWithCarerId(@Param("centerId") centerId: UUID,
                                        @Param("carerId") carerId: UUID): ChatRoomSummaryInfoProjection
}
