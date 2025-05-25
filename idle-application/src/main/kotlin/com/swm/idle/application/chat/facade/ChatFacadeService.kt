package com.swm.idle.application.chat.facade

import com.swm.idle.application.chat.domain.ChatMessageService
import com.swm.idle.application.chat.domain.ChatRoomService
import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.notification.domain.DeviceTokenService
import com.swm.idle.application.user.carer.domain.CarerService
import com.swm.idle.application.user.center.service.domain.CenterManagerService
import com.swm.idle.application.user.center.service.domain.CenterService
import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import com.swm.idle.domain.chat.event.ChatRedisTemplate
import com.swm.idle.domain.chat.vo.ChatRoomSummaryInfo
import com.swm.idle.domain.chat.vo.ReadMessage
import com.swm.idle.domain.user.carer.entity.jpa.Carer
import com.swm.idle.domain.user.center.entity.jpa.Center
import com.swm.idle.domain.user.center.entity.jpa.CenterManager
import com.swm.idle.domain.user.center.exception.CenterException
import com.swm.idle.domain.user.center.vo.BusinessRegistrationNumber
import com.swm.idle.infrastructure.fcm.chat.ChatNotificationService
import com.swm.idle.support.common.uuid.UuidCreator
import com.swm.idle.support.transfer.chat.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ChatFacadeService(
    private val centerManagerService: CenterManagerService,
    private val chatRedisTemplate: ChatRedisTemplate,
    private val messageService: ChatMessageService,
    private val notificationService: ChatNotificationService,
    private val deviceTokenService: DeviceTokenService,
    private val chatMessageService: ChatMessageService,
    private val chatroomService: ChatRoomService,
    private val centerService: CenterService,
    private val carerService: CarerService,
) {

    @Transactional
    fun send(request: SendChatMessageRequest, inputId: UUID, isCarer: Boolean) {
        val userId = if(isCarer) inputId else  getCenterId(inputId)
        val sequence = chatRedisTemplate.getChatRoomSequence(request.chatroomId)
        val message = messageService.save(request, userId, sequence)

        chatRedisTemplate.addUnreadChatRoom(request.receiverId, request.chatroomId)

        chatRedisTemplate.publish(message)

        sendNotification(message, request, isCarer)
    }

    private fun sendNotification(
        message: ChatMessage,
        request: SendChatMessageRequest,
        isCarer: Boolean
    ) {
        if(isCarer) {
            for (manager in getManagersByCenterId(UUID.fromString(request.receiverId))) {
                if (chatRedisTemplate.isChatting(manager.id)) continue

                val token = deviceTokenService.findByUserId(manager.id) ?: continue
                notificationService.send(message, request.senderName, token)
            }
        }else{
            if (chatRedisTemplate.isChatting(message.receiverId)) return
            val token = deviceTokenService.findByUserId(message.receiverId) ?: return
            notificationService.send(message, request.senderName, token)
        }
    }

    private fun getManagersByCenterId(centerId:UUID): List<CenterManager> {
        val businessNumber = BusinessRegistrationNumber(centerService.getById(centerId).businessRegistrationNumber)
        val centerManagers = centerManagerService.findAllByCenterBusinessRegistrationNumber(businessNumber)?: emptyList()
        return centerManagers
    }

    @Transactional
    fun read(request: ReadChatMessagesReqeust, inputId: UUID, isCarer: Boolean) {
        val userId = if(isCarer) inputId else  getCenterId(inputId)
        chatRedisTemplate.removeUnreadChatRoom(request.chatroomId, userId)
        chatRedisTemplate.updateReadSequence(request.chatroomId, request.sequence, userId)

        val readMessage = ReadMessage(
            chatRoomId = UUID.fromString(request.chatroomId),
            receiverId = UUID.fromString(request.opponentId),
            readUserId = userId,
            sequence = request.sequence.toLong()
        )
        chatRedisTemplate.publish(readMessage)
    }

    private fun getCenterId(managerId:UUID): UUID {
        val manager = centerManagerService.getById(managerId)
        val businessNumber = BusinessRegistrationNumber(manager.centerBusinessRegistrationNumber)
        val center = centerService.findByBusinessRegistrationNumber(businessNumber)?: throw CenterException.NotFoundException()
        return center.id
    }

    @Transactional
    fun createChatroom(request: CreateChatRoomRequest, isCarer: Boolean):CreateChatRoomResponse {
        val (carerId, centerId) =
            if (isCarer) getUserAuthentication().userId to request.opponentId
            else request.opponentId to getCenterIdByAuthentication()

        val chatRoomId = chatroomService.create(
                carerId = carerId,
                centerId = centerId
        )

        return CreateChatRoomResponse(chatRoomId)
    }

    fun getCenterIdByAuthentication():UUID {
        val managerId = getUserAuthentication().userId
        val manager = centerManagerService.getById(managerId)
        val businessNumber = BusinessRegistrationNumber(manager.centerBusinessRegistrationNumber)
        val center = centerService.findByBusinessRegistrationNumber(businessNumber)?: throw CenterException.NotFoundException()
        return center.id
    }

    @Transactional(readOnly = true)
    fun getRecentMessages(
        chatRoomId: UUID,
        messageId: UUID?,
        isCarer: Boolean
    ): ChatMessageResponse {
        val lastMessageId = messageId ?: UuidCreator.create()
        val messages = chatMessageService.getRecentMessages(chatRoomId, lastMessageId)
        val messageInfo = messages.map { ChatMessageInfo(it) }

        val chatRoom = chatroomService.getById(chatRoomId)
        val opponentId = if (isCarer) chatRoom.centerId else chatRoom.carerId
        val opponentReadSequence = chatRedisTemplate.getReadSequence(opponentId, chatRoomId)

        return ChatMessageResponse(messageInfo, opponentReadSequence)
    }

    @Transactional(readOnly = true)
    fun getChatroomSummary(isCarer: Boolean): List<ChatRoomSummaryInfo> {
        val userId = if (isCarer) getUserAuthentication().userId else getCenterIdByAuthentication()
        val unreadChatRoomIds = chatRedisTemplate.getUnreadChatRooms(userId)
        val readSequences = chatRedisTemplate.getReadSequences(userId, unreadChatRoomIds)
        val summaries = chatroomService.findChatRoomsWithLastMessages(unreadChatRoomIds, isCarer)

        val opponentIds = summaries.map { it.opponentId }.toSet()
        val opponentInfoMap = if (isCarer) {
            centerService.getByIds(opponentIds).associateBy { it.id }
        } else {
            carerService.getByIds(opponentIds).associateBy { it.id }
        }

        return summaries.map { summary ->
            val readSeq = readSequences[summary.chatRoomId.toString()] ?: 0L
            summary.count = (summary.count - readSeq).coerceAtLeast(1)

            val opponent = opponentInfoMap[summary.opponentId]
            summary.apply {
                when (opponent) {
                    is Center -> {
                        opponentName = opponent.centerName
                        opponentProfileImageUrl = opponent.profileImageUrl
                    }
                    is Carer -> {
                        opponentName = opponent.name
                        opponentProfileImageUrl = opponent.profileImageUrl
                    }
                }
            }
        }
    }
}