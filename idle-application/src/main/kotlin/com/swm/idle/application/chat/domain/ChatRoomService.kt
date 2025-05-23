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

    fun create(carerId: UUID, centerId: UUID): UUID {
        val existing = chatroomRepository.findByCarerIdAndCenterId(carerId, centerId)
        return existing?.id ?: chatroomRepository.save(ChatRoom(carerId = carerId, centerId = centerId)).id
    }

    fun getById(chatRoomId: UUID): ChatRoom {
        return chatroomRepository.findById(chatRoomId)
            .orElseThrow()
    }

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