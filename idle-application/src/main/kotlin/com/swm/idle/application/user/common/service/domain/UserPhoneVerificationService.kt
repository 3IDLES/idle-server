package com.swm.idle.application.user.common.service.domain

import com.swm.idle.application.user.vo.UserPhoneVerificationNumber
import com.swm.idle.domain.user.common.entity.redis.UserVerificationNumberRedisHash
import com.swm.idle.domain.user.common.repository.redis.UserVerificationNumberRedisRepository
import com.swm.idle.domain.user.common.vo.PhoneNumber
import org.springframework.stereotype.Service

@Service
class UserPhoneVerificationService(
    private val userVerificationNumberRedisRepository: UserVerificationNumberRedisRepository,
) {

    fun save(
        phoneNumber: PhoneNumber,
        userPhoneVerificationNumber: UserPhoneVerificationNumber,
        expireSeconds: Long,
    ) {
        val userVerificationNumberRedisHash = UserVerificationNumberRedisHash(
            phoneNumber = phoneNumber.value,
            verificationNumber = userPhoneVerificationNumber.value,
            expireSeconds = expireSeconds,
        )

        userVerificationNumberRedisRepository.save(userVerificationNumberRedisHash)
    }

    fun findByPhoneNumber(phoneNumber: PhoneNumber): Pair<PhoneNumber, UserPhoneVerificationNumber>? {
        return userVerificationNumberRedisRepository
            .findById(phoneNumber.value)
            .map {
                PhoneNumber(it.phoneNumber) to UserPhoneVerificationNumber(it.verificationNumber)
            }
            .orElse(null)
    }

    fun deleteByPhoneNumber(phoneNumber: PhoneNumber) {
        userVerificationNumberRedisRepository.deleteById(phoneNumber.value)
    }

}
