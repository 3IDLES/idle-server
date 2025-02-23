package com.swm.idle.application.chat.facade

import com.swm.idle.application.chat.domain.ChatMessageService
import com.swm.idle.application.chat.domain.ChatRoomService
import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.notification.domain.DeviceTokenService
import com.swm.idle.application.user.carer.domain.CarerService
import com.swm.idle.application.user.center.service.domain.CenterService
import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import com.swm.idle.domain.chat.event.ChatRedisPublisher
import com.swm.idle.domain.chat.vo.ChatRoomSummaryInfo
import com.swm.idle.domain.chat.vo.ReadMessage
import com.swm.idle.infrastructure.fcm.chat.ChatNotificationService
import com.swm.idle.support.common.uuid.UuidCreator
import com.swm.idle.support.transfer.chat.*
import kotlinx.coroutines.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class ChatFacadeService(
    private val redisPublisher: ChatRedisPublisher,
    private val messageService: ChatMessageService,
    private val notificationService: ChatNotificationService,
    private val deviceTokenService: DeviceTokenService,
    private val chatMessageService: ChatMessageService,
    private val chatroomService: ChatRoomService,
    private val centerService: CenterService,
    private val carerService: CarerService,
) {

    @Transactional
    fun sendMessage(request: SendChatMessageRequest, userId: UUID) {
        val message = ChatMessage(
            chatRoomId = UUID.fromString(request.chatroomId),
            content = request.content,
            senderId = userId,
            receiverId = UUID.fromString(request.receiverId),
        )
        runBlocking{
            launch { messageService.save(message) }
            launch { redisPublisher.publish(message) }
            launch {
                val token = deviceTokenService.findByUserId(message.receiverId)
                notificationService.send(message, request.senderName, token)
            }
        }

    }

    @Transactional
    fun readMessage(request: ReadChatMessagesReqeust, userId: UUID){
        runBlocking {
            launch { messageService.read(request, userId) }
            launch {
                val redisMessage = ReadMessage(
                    chatRoomId = request.chatRoomId,
                    receiverId = request.opponentId,
                    readUserId = userId)
                redisPublisher.publish(redisMessage)
            }
        }
    }

    @Transactional
    fun createChatroom(request: CreateChatRoomRequest, isCarer: Boolean):CreateChatRoomResponse {
        val (carerId, centerId) = if (isCarer) {
            getUserAuthentication().userId to request.opponentId
        } else {
            request.opponentId to getUserAuthentication().userId
        }

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
        val userId = getUserAuthentication().userId
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

    fun getSingleChatRoomInfo(chatRoomId: UUID, opponentId: UUID,isCarer: Boolean): ChatRoomSummaryInfo {
        val (carerId, centerId) = if (isCarer) {
            getUserAuthentication().userId to opponentId
        } else {
            opponentId to getUserAuthentication().userId
        }

        val chatRoomSummaryInfo = chatroomService.getByCenterWithCarer(
            centerId = centerId,
            carerId =carerId,
            isCarer)

        return if (isCarer) {
            val center = centerService.getById(centerId)
            chatRoomSummaryInfo.also {
                it.opponentName = center.centerName
                it.opponentProfileImageUrl = center.profileImageUrl
            }

        }else {
            val carer = carerService.getById(carerId)
            chatRoomSummaryInfo.also {
                it.opponentName = carer.name
                it.opponentProfileImageUrl = carer.profileImageUrl
            }
        }
    }
}