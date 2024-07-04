package com.swm.idle.domain.user.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive

@RedisHash("sms_verification_info")
class UserSmsVerificationNumberRedisHash(
    phoneNumber: String,
    verificationNumber: String,
    expireSeconds: Long,
) {

    @Id
    var phoneNumber: String = phoneNumber
        private set

    var verificationNumber: String = verificationNumber
        private set

    @TimeToLive
    var expireSeconds: Long = expireSeconds
        private set

}
