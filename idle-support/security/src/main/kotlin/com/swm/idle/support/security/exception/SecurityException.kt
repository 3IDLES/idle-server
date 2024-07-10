package com.swm.idle.support.security.exception

import com.swm.idle.support.common.exception.CustomException

sealed class SecurityException(
    codeNumber: Int,
    message: String,
) : CustomException(CODE_PREFIX, codeNumber, message) {

    class UnAuthorizedRequest(message: String = "인증되지 않은 사용자입니다.") :
        SecurityException(codeNumber = 1, message = message)

    class InvalidLoginRequest(message: String = "올바르지 않은 아이디 또는 비밀번호입니다.") :
        SecurityException(codeNumber = 2, message = message)

    class InvalidPassword(message: String = "올바르지 않은 비밀번호입니다.") :
        SecurityException(codeNumber = 3, message = message)

    companion object {
        const val CODE_PREFIX = "SECURITY"
    }

}
