package com.swm.idle.config

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import redis.embedded.RedisServer

@Configuration
@Profile("test")
class EmbeddedRedisConfig {

    private val redisPort = 6379
    private val redisServer = RedisServer(redisPort)

    @PostConstruct
    fun startRedis() {
        if (!redisServer.isActive) {
            redisServer.start()
        }
    }

    @PreDestroy
    fun stopRedis() {
        redisServer.stop()
    }
}
