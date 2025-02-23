package com.swm.idle.application.chat.domain

import com.swm.idle.domain.chat.entity.jpa.ChatRoom
import com.swm.idle.domain.chat.repository.ChatRoomRepository
import com.swm.idle.domain.chat.vo.ChatRoomSummaryInfo
import com.swm.idle.domain.chat.vo.ChatRoomSummaryInfoProjection
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatRoomService (val chatroomRepository: ChatRoomRepository){

    fun create(carerId: UUID, centerId:UUID):UUID {
        val chatRoom = ChatRoom(
            carerId = carerId,
            centerId = centerId,
        )
        return chatroomRepository.save(chatRoom).id
    }

    fun findChatroomSummaries(userId: UUID, isCarer: Boolean): List<ChatRoomSummaryInfo> {
        val projections: List<ChatRoomSummaryInfoProjection>
        if(isCarer) {
            projections = chatroomRepository.findCaresChatroomSummaries(userId)
        }else {
            projections = chatroomRepository.findCentersChatroomSummaries(userId)
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

    fun getByCenterWithCarer(centerId: UUID, carerId: UUID, isCarer: Boolean): ChatRoomSummaryInfo {
        val projections: ChatRoomSummaryInfoProjection

        if(isCarer) {
            projections = chatroomRepository.carerFindByCenterIdWithCarerId(
                centerId = centerId,
                carerId = carerId
            )
        }else {
            projections = chatroomRepository.centerFindByCenterIdWithCarerId(
                centerId = centerId,
                carerId = carerId
            )
        }

        return mappingChatRoomSummaryInfo(projections)
    }
}