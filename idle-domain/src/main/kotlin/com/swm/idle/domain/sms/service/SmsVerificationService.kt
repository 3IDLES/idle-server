package com.swm.idle.domain.sms.service

import com.swm.idle.domain.sms.entity.SmsVerificationNumberRedisHash
import com.swm.idle.domain.sms.repository.SmsVerificationNumberRedisRepository
import com.swm.idle.domain.sms.vo.PhoneNumber
import com.swm.idle.domain.sms.vo.SmsVerificationNumber
import org.springframework.stereotype.Service

@Service
class SmsVerificationService(
    private val smsVerificationNumberRedisRepository: SmsVerificationNumberRedisRepository,
) {

    fun save(
        phoneNumber: PhoneNumber,
        smsVerificationNumber: SmsVerificationNumber,
        expireSeconds: Long,
    ) {
        val smsVerificationNumberRedisHash = SmsVerificationNumberRedisHash(
            phoneNumber = phoneNumber.value,
            verificationNumber = smsVerificationNumber.value,
            expireSeconds = expireSeconds,
        )

        smsVerificationNumberRedisRepository.save(smsVerificationNumberRedisHash)
    }

    fun findByPhoneNumber(phoneNumber: PhoneNumber): Pair<PhoneNumber, SmsVerificationNumber>? {
        return smsVerificationNumberRedisRepository
            .findById(phoneNumber.value)
            .map {
                PhoneNumber(it.phoneNumber) to SmsVerificationNumber(it.verificationNumber)
            }
            .orElse(null)
    }

    fun deleteByPhoneNumber(phoneNumber: PhoneNumber) {
        smsVerificationNumberRedisRepository.deleteById(phoneNumber.value)
    }

}
