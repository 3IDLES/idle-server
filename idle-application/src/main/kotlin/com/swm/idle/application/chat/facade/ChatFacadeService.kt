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
    fun carerSend(request: SendChatMessageRequest, userId: UUID) {
        val message = messageService.save(request, userId)
        chatRedisTemplate.publish(message)

        for(centerManager in centerManagers()) {
            if (chatRedisTemplate.isChatting(centerManager.id)) continue

            val token = deviceTokenService.findByUserId(centerManager.id)
            notificationService.send(message, request.senderName, token)
        }
    }

    private fun centerManagers() = centerManagerService
        .findAllByCenterBusinessRegistrationNumber(
            BusinessRegistrationNumber(
                getCenter().businessRegistrationNumber
            )
        )?: emptyList()

    @Transactional
    fun centerSend(request: SendChatMessageRequest, userId: UUID) {
        val message = messageService.save(request, getCenter().id)
        chatRedisTemplate.publish(message)

        if (chatRedisTemplate.isChatting(message.receiverId)) return

        val token = deviceTokenService.findByUserId(message.receiverId)
        notificationService.send(message, request.senderName, token)
    }

    @Transactional
    fun carerRead(request: ReadChatMessagesReqeust, userId: UUID) {
        messageService.read(request, userId)

        val readMessage = ReadMessage(
            chatRoomId = request.chatroomId,
            receiverId = request.opponentId,
            readUserId = userId
        )
        chatRedisTemplate.publish(readMessage)
    }

    @Transactional
    fun centerRead(request: ReadChatMessagesReqeust, userId: UUID) {
        messageService.read(request, getCenter().id)

        val readMessage = ReadMessage(
            chatRoomId = request.chatroomId,
            receiverId = request.opponentId,
            readUserId = userId
        )
        chatRedisTemplate.publish(readMessage)
    }

    @Transactional
    fun createChatroom(request: CreateChatRoomRequest, isCarer: Boolean):CreateChatRoomResponse {
        val (carerId, centerId) =
            if (isCarer) getUserAuthentication().userId to request.opponentId
            else request.opponentId to getCenter().id

        val chatRoomId = chatroomService.create(
                carerId = carerId,
                centerId = centerId
        )

        return CreateChatRoomResponse(chatRoomId)
    }

    fun getRecentMessages(chatRoomId: UUID, messageId: UUID?): List<ChatMessageResponse> {
        val effectiveMessageId = messageId ?: UuidCreator.create()

        return chatMessageService.getRecentMessages(chatRoomId, effectiveMessageId)
            .map { ChatMessageResponse(it) }
    }

    fun getChatroomSummary(isCarer: Boolean): List<ChatRoomSummaryInfo> {
        val userId: UUID =
            if (isCarer) getUserAuthentication().userId
            else getCenter().id


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

    private fun getCenter() = getUserAuthentication().userId.let { centerManagerId ->
        centerManagerService.getById(centerManagerId).let {
            centerService.findByBusinessRegistrationNumber(
                BusinessRegistrationNumber(it.centerBusinessRegistrationNumber)
            ) ?: throw CenterException.NotFoundException()
        }
    }
}