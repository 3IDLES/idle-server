package com.swm.idle.domain.user.service

import com.swm.idle.domain.user.entity.UserSmsVerificationNumberRedisHash
import com.swm.idle.domain.user.repository.UserSmsVerificationNumberRedisRepository
import com.swm.idle.domain.user.vo.PhoneNumber
import com.swm.idle.domain.user.vo.UserSmsVerificationNumber
import org.springframework.stereotype.Service

@Service
class UserSmsVerificationService(
    private val userSmsVerificationNumberRedisRepository: UserSmsVerificationNumberRedisRepository,
) {

    fun save(
        phoneNumber: PhoneNumber,
        userSmsVerificationNumber: UserSmsVerificationNumber,
        expireSeconds: Long,
    ) {
        val userSmsVerificationNumberRedisHash = UserSmsVerificationNumberRedisHash(
            phoneNumber = phoneNumber.value,
            verificationNumber = userSmsVerificationNumber.value,
            expireSeconds = expireSeconds,
        )

        userSmsVerificationNumberRedisRepository.save(userSmsVerificationNumberRedisHash)
    }

    fun findByPhoneNumber(phoneNumber: PhoneNumber): Pair<PhoneNumber, UserSmsVerificationNumber>? {
        return userSmsVerificationNumberRedisRepository
            .findById(phoneNumber.value)
            .map {
                PhoneNumber(it.phoneNumber) to UserSmsVerificationNumber(it.verificationNumber)
            }
            .orElse(null)
    }

}
