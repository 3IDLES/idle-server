package com.swm.idle.domain.user.common.exception

import com.swm.idle.support.common.exception.CustomException

sealed class UserException(
    codeNumber: Int,
    message: String,
) : CustomException(CODE_PREFIX, codeNumber, message) {

    class InvalidVerificationNumber(message: String = "잘못된 인증번호입니다.") :
        UserException(codeNumber = 1, message = message)

    class VerificationNumberNotFound(message: String = "인증번호가 만료되었거나 존재하지 않습니다.") :
        UserException(codeNumber = 2, message = message)

    companion object {
        const val CODE_PREFIX = "USER"
    }

}
