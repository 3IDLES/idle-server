package com.swm.idle.domain.chat.repository

import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import io.lettuce.core.dynamic.annotation.Param
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChatMessageRepository : JpaRepository<ChatMessage, UUID> {
    /**
                          * Retrieves up to 50 most recent chat messages from a specific chat room with IDs less than the given message ID.
                          *
                          * @param chatroomId The unique identifier of the chat room.
                          * @param messageId The message ID threshold; only messages with IDs less than this value are returned.
                          * @return A list of up to 50 `ChatMessage` entities ordered by descending ID.
                          */
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