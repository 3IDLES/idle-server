package com.swm.idle

import com.swm.idle.config.DomainTestApplication
import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import com.swm.idle.domain.chat.repository.ChatMessageRepository
import io.kotest.core.spec.style.BehaviorSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [DomainTestApplication::class])
@ActiveProfiles("test")
class ChatTest : BehaviorSpec() {
    @Autowired
    lateinit var chatMessageRepository: ChatMessageRepository

    val chatRoomId = UUID.randomUUID()
    val senderId = UUID.randomUUID()
    val receiverId = UUID.randomUUID()


    init {
        given("채팅 메시지가 여러개 주어졌을 때") {
            val msg1 = ChatMessage(chatRoomId = chatRoomId, senderId = senderId, receiverId = receiverId, content = "1")
            val msg2 = ChatMessage(chatRoomId = chatRoomId, senderId = senderId, receiverId = receiverId, content = "2")
            val msg3 = ChatMessage(chatRoomId = chatRoomId, senderId = senderId, receiverId = UUID.randomUUID(), content = "3")
            chatMessageRepository.saveAll(listOf(msg1, msg2, msg3))

            `when`("readByChatroomId 메서드로 읽음 처리 하면") {
                chatMessageRepository.readByChatroomId(chatRoomId, receiverId)

                then("해당 메시지들의 isRead 상태가 true로 업데이트 되어야 한다") {
                    val updatedMessages = chatMessageRepository.findAll()
                    val readMessages = updatedMessages.filter { it.receiverId == receiverId && it.chatRoomId == chatRoomId }
                    assertThat(readMessages).allMatch { it.isRead }
                }
            }
        }
    }
}
