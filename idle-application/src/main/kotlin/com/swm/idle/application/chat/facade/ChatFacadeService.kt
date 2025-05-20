package com.swm.idle.application.chat.facade

import com.swm.idle.application.chat.domain.ChatMessageService
import com.swm.idle.application.chat.domain.ChatRoomService
import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.notification.domain.DeviceTokenService
import com.swm.idle.application.user.carer.domain.CarerService
import com.swm.idle.application.user.center.service.domain.CenterManagerService
import com.swm.idle.application.user.center.service.domain.CenterService
import com.swm.idle.domain.chat.event.ChatRedisTemplate
import com.swm.idle.domain.chat.vo.ChatRoomSummaryInfo
import com.swm.idle.domain.chat.vo.ReadMessage
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
@Transactional(readOnly = true)
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
    fun carerSend(request: SendChatMessageRequest, carerId: UUID) {
        val message = messageService.save(request, carerId)
        chatRedisTemplate.publish(message)

        for(manager in getManagersByCenterId(UUID.fromString(request.receiverId))) {
            if (chatRedisTemplate.isChatting(manager.id)) continue

            val token = deviceTokenService.findByUserId(manager.id)?:continue
            notificationService.send(message, request.senderName, token)
        }
    }

    private fun getManagersByCenterId(centerId:UUID): List<CenterManager> {
        val businessNumber = BusinessRegistrationNumber(centerService.getById(centerId).businessRegistrationNumber)
        val centerManagers = centerManagerService.findAllByCenterBusinessRegistrationNumber(businessNumber)?: emptyList()
        return centerManagers
    }

    @Transactional
    fun centerSend(request: SendChatMessageRequest, managerId: UUID) {
        val centerId = getCenterId(managerId)
        val message = messageService.save(request, centerId)
        chatRedisTemplate.publish(message)

        if (chatRedisTemplate.isChatting(message.receiverId)) return

        val token = deviceTokenService.findByUserId(message.receiverId)?:return
        notificationService.send(message, request.senderName, token)
    }

    @Transactional
    fun carerRead(request: ReadChatMessagesReqeust, carerId: UUID) {
        messageService.read(request, carerId)

        val readMessage = ReadMessage(
            chatRoomId = UUID.fromString(request.chatroomId),
            receiverId = UUID.fromString(request.opponentId),
            readUserId = carerId
        )
        chatRedisTemplate.publish(readMessage)
    }

    @Transactional
    fun centerRead(request: ReadChatMessagesReqeust, managerId: UUID) {
        val centerId = getCenterId(managerId)
        messageService.read(request, centerId)

        val readMessage = ReadMessage(
            chatRoomId = UUID.fromString(request.chatroomId),
            receiverId = UUID.fromString(request.opponentId),
            readUserId = centerId
        )
        chatRedisTemplate.publish(readMessage)
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

    fun getRecentMessages(chatRoomId: UUID, messageId: UUID?): List<ChatMessageResponse> {
        val effectiveMessageId = messageId ?: UuidCreator.create()

        return chatMessageService.getRecentMessages(chatRoomId, effectiveMessageId)
            .map { ChatMessageResponse(it) }
    }

    fun getChatroomSummary(isCarer: Boolean): List<ChatRoomSummaryInfo> {
        val userId: UUID = if (isCarer) getUserAuthentication().userId
        else getCenterIdByAuthentication()

        val summary = chatroomService.findChatroomSummaries(userId, isCarer)

        return if (isCarer) {
            summary.map {
                val center = centerService.getById(it.opponentId)
                it.copy(opponentName = center.centerName, opponentProfileImageUrl = center.profileImageUrl)
            }
        } else {
            summary.map {
                val carer = carerService.getById(it.opponentId)
                it.copy(opponentName = carer.name, opponentProfileImageUrl = carer.profileImageUrl)
            }
        }
    }

    private fun getCenterId(managerId:UUID): UUID {
        val manager = centerManagerService.getById(managerId)
        val businessNumber = BusinessRegistrationNumber(manager.centerBusinessRegistrationNumber)
        val center = centerService.findByBusinessRegistrationNumber(businessNumber)?: throw CenterException.NotFoundException()
        return center.id
    }
}