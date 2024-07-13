package com.swm.idle.infrastructure.sms.common.exception

import com.swm.idle.support.common.exception.CustomException

sealed class SmsException(
    codeNumber: Int,
    message: String,
) : CustomException(CODE_PREFIX, codeNumber, message) {

    class ClientException(message: String = "인증 번호 발송에 실패하였습니다.") :
        SmsException(codeNumber = 1, message = message)

    companion object {
        const val CODE_PREFIX = "SMS"
    }

}
