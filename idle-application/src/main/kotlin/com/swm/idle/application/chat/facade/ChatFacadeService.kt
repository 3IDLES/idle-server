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

    /**
     * Sends a chat message from either a carer or a center user.
     *
     * Determines the sender's user ID based on the sender type, saves the message with a sequence number, updates unread chat room status, publishes the message, and triggers notification delivery to the recipient.
     *
     * @param request The chat message request containing message and recipient details.
     * @param inputId The UUID of the sender (carer or center manager).
     * @param isCarer Indicates whether the sender is a carer (`true`) or a center user (`false`).
     */
    @Transactional
    fun send(request: SendChatMessageRequest, inputId: UUID, isCarer: Boolean) {
        val userId = if(isCarer) inputId else  getCenterId(inputId)
        val sequence = chatRedisTemplate.getChatRoomSequence(request.chatroomId)
        val message = messageService.save(request, userId, sequence)

        chatRedisTemplate.addUnreadChatRoom(request.receiverId, request.chatroomId)

        chatRedisTemplate.publish(message)

        sendNotification(message, request, isCarer)
    }

    /**
     * Sends a push notification for a chat message to the appropriate recipient(s) if they are not actively chatting.
     *
     * If the sender is a carer, notifications are sent to all center managers of the receiving center who are not currently in the chat. If the sender is a center, a notification is sent to the receiving carer if they are not currently in the chat. No notification is sent if the recipient is actively chatting or if no device token is found.
     *
     * @param message The chat message to notify about.
     * @param request The original chat message request containing sender and receiver information.
     * @param isCarer Indicates whether the sender is a carer.
     */
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

    /**
     * Retrieves all center managers associated with the specified center ID.
     *
     * @param centerId The unique identifier of the center.
     * @return A list of center managers for the given center, or an empty list if none are found.
     */
    private fun getManagersByCenterId(centerId:UUID): List<CenterManager> {
        val businessNumber = BusinessRegistrationNumber(centerService.getById(centerId).businessRegistrationNumber)
        val centerManagers = centerManagerService.findAllByCenterBusinessRegistrationNumber(businessNumber)?: emptyList()
        return centerManagers
    }

    /**
     * Marks chat messages as read for a user in a chat room and updates the read sequence in Redis.
     *
     * Removes the chat room from the user's unread list, updates the user's read sequence for the chat room,
     * and publishes a read event to notify other participants.
     *
     * @param request The request containing chat room ID, opponent ID, and the sequence number up to which messages are read.
     * @param inputId The UUID of the acting user (carer or center manager).
     * @param isCarer Indicates whether the acting user is a carer (`true`) or a center manager (`false`).
     */
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

    /**
     * Retrieves the center ID associated with the given center manager ID.
     *
     * @param managerId The UUID of the center manager.
     * @return The UUID of the center managed by the specified manager.
     * @throws CenterException.NotFoundException if no center is found for the manager's business registration number.
     */
    private fun getCenterId(managerId:UUID): UUID {
        val manager = centerManagerService.getById(managerId)
        val businessNumber = BusinessRegistrationNumber(manager.centerBusinessRegistrationNumber)
        val center = centerService.findByBusinessRegistrationNumber(businessNumber)?: throw CenterException.NotFoundException()
        return center.id
    }

    /**
     * Creates a new chat room between a carer and a center based on the sender's role.
     *
     * Determines the carer and center IDs according to the sender's authentication and the provided opponent ID, then creates the chat room and returns its ID.
     *
     * @param request The request containing the opponent's ID.
     * @param isCarer Indicates whether the sender is a carer.
     * @return The response containing the created chat room's ID.
     */
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

    /**
     * Retrieves the center ID associated with the currently authenticated center manager.
     *
     * @return The UUID of the center linked to the authenticated manager.
     * @throws CenterException.NotFoundException if no center is found for the manager's business registration number.
     */
    fun getCenterIdByAuthentication():UUID {
        val managerId = getUserAuthentication().userId
        val manager = centerManagerService.getById(managerId)
        val businessNumber = BusinessRegistrationNumber(manager.centerBusinessRegistrationNumber)
        val center = centerService.findByBusinessRegistrationNumber(businessNumber)?: throw CenterException.NotFoundException()
        return center.id
    }

    /**
     * Retrieves recent chat messages for a chat room along with the opponent's read sequence.
     *
     * If `messageId` is null, a new UUID is generated to fetch messages. The opponent's ID is determined based on the sender's role, and their read sequence is retrieved from Redis.
     *
     * @param chatRoomId The ID of the chat room to fetch messages from.
     * @param messageId The message ID to start fetching from; if null, a new UUID is used.
     * @param isCarer Indicates whether the requester is a carer.
     * @return A response containing the list of recent chat messages and the opponent's read sequence.
     */
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

    /**
     * Retrieves a summary of chat rooms for the authenticated user, including unread message counts and opponent details.
     *
     * For each chat room, the summary includes the last message, adjusted unread message count based on read sequences, and opponent's name and profile image. Opponent information is determined by the user's role (carer or center).
     *
     * @param isCarer Indicates whether the authenticated user is a carer.
     * @return A list of chat room summaries enriched with opponent information and unread message counts.
     */
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