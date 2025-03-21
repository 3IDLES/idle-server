package com.swm.idle.infrastructure.sms.auth.service

import com.swm.idle.domain.user.common.vo.PhoneNumber
import com.swm.idle.infrastructure.sms.common.exception.SmsException
import com.swm.idle.infrastructure.sms.common.properties.SmsVerificationProperties
import com.swm.idle.infrastructure.sms.common.vo.SmsVerificationInfo
import com.swm.idle.infrastructure.sms.util.SmsClient
import org.springframework.stereotype.Service
import java.security.SecureRandom

@Service
class SmsService(
    private val smsClient: SmsClient,
    private val smsVerificationProperties: SmsVerificationProperties,
) {

    fun sendVerificationMessage(phoneNumber: PhoneNumber): SmsVerificationInfo {
        val smsVerificationNumber = generateVerificationNumber()

        runCatching {
            smsClient.sendMessage(
                to = phoneNumber.value.replace("-", ""),
                content = CENTER_VERIFICATION_MESSAGE_FORMAT.format(smsVerificationNumber),
            )
        }.onFailure {
            throw SmsException.ClientException()
        }

        return SmsVerificationInfo(
            phoneNumber = phoneNumber,
            userPhoneVerificationNumber = smsVerificationNumber,
            expireSeconds = smsVerificationProperties.expireSeconds,
        )
    }

    fun generateVerificationNumber(): String {
        val random = SecureRandom()

        return (MINIMUM_VERIFICATION_NUMBER + random.nextInt(VERIFICATION_NUMBER_SCALE)).toString()
    }

    companion object {

        const val MINIMUM_VERIFICATION_NUMBER = 100_000
        const val VERIFICATION_NUMBER_SCALE = 900_000
        const val CENTER_VERIFICATION_MESSAGE_FORMAT = "[케어밋] 요청하신 인증번호는 %s 입니다."
    }

}

