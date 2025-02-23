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
import com.swm.idle.support.transfer.chat.CreateChatRoomRequest
import com.swm.idle.support.transfer.chat.ReadChatMessagesReqeust
import com.swm.idle.support.transfer.chat.SendChatMessageRequest
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
        val readRequest = ReadMessage(
            request.chatRoomId,
            userId
        )
        runBlocking {
            launch { messageService.read(request, userId) }
            launch { redisPublisher.publish(readRequest) }
        }
    }

    @Transactional
    fun createChatroom(request: CreateChatRoomRequest, isCarer: Boolean) {
        val userId = getUserAuthentication().userId

        if(isCarer) {
            chatroomService.create(
                carerId = userId,
                centerId = request.opponentId,
            )
        }else{
            chatroomService.create(
                centerId = userId,
                carerId = request.opponentId,
            )
        }
    }

    fun getRecentMessages(chatRoomId: UUID, messageId: UUID?): List<ChatMessage> {
        if(messageId == null){
            val newMessageId = UuidCreator.create()
            return chatMessageService.getRecentMessages(chatRoomId, newMessageId)
        }
        return chatMessageService.getRecentMessages(chatRoomId, messageId)
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
}