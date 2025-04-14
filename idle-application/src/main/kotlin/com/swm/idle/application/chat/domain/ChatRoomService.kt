package com.swm.idle.application.chat.domain

import com.swm.idle.domain.chat.entity.jpa.ChatRoom
import com.swm.idle.domain.chat.repository.ChatRoomRepository
import com.swm.idle.domain.chat.vo.ChatRoomSummaryInfo
import com.swm.idle.domain.chat.vo.ChatRoomSummaryInfoProjection
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatRoomService (val chatroomRepository: ChatRoomRepository){

    fun create(carerId: UUID, centerId: UUID): UUID {
        val existing = chatroomRepository.findByCarerIdAndCenterId(carerId, centerId)
        return existing?.id ?: chatroomRepository.save(ChatRoom(carerId = carerId, centerId = centerId)).id
    }


    fun findChatroomSummaries(userId: UUID, isCarer: Boolean): List<ChatRoomSummaryInfo> {
        val projections: List<ChatRoomSummaryInfoProjection>
        if(isCarer) {
            projections = chatroomRepository.carerFindChatRooms(userId)
        }else {
            projections = chatroomRepository.centerFindChatRooms(userId)
        }

        return projections.map { projection ->
            mappingChatRoomSummaryInfo(projection)
        }
    }

    private fun mappingChatRoomSummaryInfo(projection: ChatRoomSummaryInfoProjection) =
        ChatRoomSummaryInfo(
            chatRoomId = projection.getChatRoomId(),
            lastMessage = projection.getLastMessage(),
            lastMessageTime = projection.getLastMessageTime(),
            count = projection.getUnreadCount(),
            opponentId = projection.getOpponentId()
        )
}