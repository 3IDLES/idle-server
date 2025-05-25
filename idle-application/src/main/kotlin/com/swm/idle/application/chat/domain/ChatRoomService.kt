package com.swm.idle.application.chat.domain

import com.swm.idle.domain.chat.entity.jpa.ChatRoom
import com.swm.idle.domain.chat.repository.ChatRoomRepository
import com.swm.idle.domain.chat.vo.ChatRoomSummaryInfo
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatRoomService(
    val chatroomRepository: ChatRoomRepository,
    private val chatRoomRepository: ChatRoomRepository
){

    /**
     * Creates a chat room for the given carer and center IDs if one does not already exist.
     *
     * If a chat room already exists for the specified carer and center, returns its ID; otherwise, creates a new chat room and returns its ID.
     *
     * @param carerId The UUID of the carer.
     * @param centerId The UUID of the center.
     * @return The UUID of the existing or newly created chat room.
     */
    fun create(carerId: UUID, centerId: UUID): UUID {
        val existing = chatroomRepository.findByCarerIdAndCenterId(carerId, centerId)
        return existing?.id ?: chatroomRepository.save(ChatRoom(carerId = carerId, centerId = centerId)).id
    }

    /**
     * Retrieves a chat room by its unique identifier.
     *
     * @param chatRoomId The UUID of the chat room to retrieve.
     * @return The corresponding ChatRoom entity.
     * @throws NoSuchElementException if no chat room with the given ID exists.
     */
    fun getById(chatRoomId: UUID): ChatRoom {
        return chatroomRepository.findById(chatRoomId)
            .orElseThrow()
    }

    /**
     * Retrieves summaries of chat rooms with their latest messages for the given room IDs.
     *
     * Converts the provided set of string room IDs to UUIDs, fetches chat rooms and their last messages,
     * and constructs summary information for each. The opponent ID in each summary is determined by the `isCarer` flag.
     *
     * @param roomIds Set of chat room IDs as strings.
     * @param isCarer Indicates whether the requesting user is a carer (true) or a center (false).
     * @return List of chat room summaries, each containing the chat room ID, opponent ID, last message content,
     * last message timestamp, and last message sequence number.
     */
    fun findChatRoomsWithLastMessages(roomIds: Set<String>, isCarer: Boolean): List<ChatRoomSummaryInfo> {
        val uuidSet = roomIds.map(UUID::fromString).toSet()
        val projections = chatRoomRepository.findChatRoomsWithLastMessages(uuidSet)

        return projections.map { projection ->
            val opponentId = if (isCarer) projection.getCenterId() else projection.getCarerId()
            ChatRoomSummaryInfo(
                projection.getChatRoomId(),
                opponentId,
                projection.getLastMessage(),
                projection.getLastMessageTime(),
                projection.getLastSequence()
            )
        }
    }

}