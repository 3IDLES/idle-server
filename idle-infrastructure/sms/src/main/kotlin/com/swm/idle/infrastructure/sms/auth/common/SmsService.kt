package com.swm.idle.infrastructure.sms.auth.common

import com.swm.idle.domain.user.vo.PhoneNumber
import com.swm.idle.domain.user.vo.SmsVerificationNumber
import com.swm.idle.infrastructure.sms.util.SmsClient
import org.springframework.stereotype.Service
import java.security.SecureRandom

@Service
class SmsService(
    private val smsClient: SmsClient
) {

    fun sendVerificationMessage(phoneNumber: PhoneNumber) {
        val smsVerificationNumber = generateVerificationNumber()

        smsClient.sendMessage(
            to = phoneNumber.value.replace("-", ""),
            content = CENTER_VERIFICATION_MESSAGE_FORMAT.format(smsVerificationNumber.value),
        )
    }

    fun generateVerificationNumber(): SmsVerificationNumber {
        val random = SecureRandom()

        return SmsVerificationNumber(
            MINIMUM_VERIFICATION_NUMBER + random.nextInt(
                VERIFICATION_NUMBER_SCALE
            ))
    }

    companion object {
        const val MINIMUM_VERIFICATION_NUMBER = 100_000
        const val VERIFICATION_NUMBER_SCALE = 900_000
        const val CENTER_VERIFICATION_MESSAGE_FORMAT = "[케어밋] 센터 회원가입 인증번호는 %s 입니다."
    }

}

