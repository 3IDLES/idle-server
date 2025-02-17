package com.swm.idle.domain.chat.repository

import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChatMessageRepository : JpaRepository<ChatMessage, UUID> {
    @Modifying
    @Query("""
    UPDATE ChatMessage cm 
    SET cm.isRead = true 
    WHERE cm.chatRoomId = :chatroomId 
    AND cm.isRead = false
    AND cm.receiverId = :readUserId 
    """)
    fun readByChatroomId(@Param("chatroomId") chatroomId: UUID, @Param("readUserId") readUserId: UUID)

    @Query(value = """
    SELECT * 
    FROM chat_message 
    WHERE chat_room_id = :chatroomId 
    AND id < :messageId
    ORDER BY id DESC
    LIMIT 50
""", nativeQuery = true)
    fun getRecentMessages(@Param("chatroomId") chatroomId: UUID,
                         @Param("messageId") messageId: UUID): List<ChatMessage>
}