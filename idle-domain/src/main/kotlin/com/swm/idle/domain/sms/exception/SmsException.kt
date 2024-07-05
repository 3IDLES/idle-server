package com.swm.idle.domain.sms.exception

import com.swm.idle.support.common.exception.CustomException

sealed class SmsException(
    codeNumber: Int,
    message: String,
) : CustomException(CODE_PREFIX, codeNumber, message) {

    class InvalidSmsVerificationNumber(message: String = "잘못된 인증번호입니다.") :
        SmsException(codeNumber = 1, message = message)

    class SmsVerificationNumberNotFound(message: String = "인증번호가 만료되었거나 존재하지 않습니다.") :
        SmsException(codeNumber = 2, message = message)

    companion object {
        const val CODE_PREFIX = "SMS"
    }
}
