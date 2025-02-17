package com.swm.idle.domain.chat.config

import com.swm.idle.domain.chat.entity.jpa.ChatMessage
import com.swm.idle.domain.chat.event.ChatRedisSubscriber
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class ChatRedisConfig {

    @Bean
    fun chatChannelTopic(): ChannelTopic {
        return ChannelTopic(CHATROOM)
    }

    @Bean
    fun redisListenerContainer(
        connectionFactory: RedisConnectionFactory,
        messageListenerAdapter: MessageListenerAdapter,
    ): RedisMessageListenerContainer {
        return RedisMessageListenerContainer().also {
            it.addMessageListener(messageListenerAdapter, chatChannelTopic())
            it.setConnectionFactory(connectionFactory)
        }
    }

    @Bean
    fun messageListenerAdapter(chatSubscriber: ChatRedisSubscriber): MessageListenerAdapter {
        return MessageListenerAdapter(chatSubscriber,"onMessage")
    }

    @Bean
    fun chatRoomRedisTemplate(redisConnectionFactory: RedisConnectionFactory):RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = redisConnectionFactory

        template.keySerializer = StringRedisSerializer()
        template.hashKeySerializer = StringRedisSerializer()

        val serializer = Jackson2JsonRedisSerializer(ChatMessage::class.java)
        template.valueSerializer = serializer
        template.hashValueSerializer = serializer

        return template
    }

    companion object {
        const val CHATROOM = "chatroom"
    }
}
