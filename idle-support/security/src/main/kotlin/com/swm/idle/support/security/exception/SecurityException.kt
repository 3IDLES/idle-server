package com.swm.idle.support.security.exception

import com.swm.idle.support.common.exception.CustomException

sealed class SecurityException(
    codeNumber: Int,
    message: String,
) : CustomException(CODE_PREFIX, codeNumber, message) {

    class UnAuthorizedRequest(message: String = "인증되지 않은 사용자입니다.") :
        SecurityException(codeNumber = 1, message = message)

    companion object {
        const val CODE_PREFIX = "SECURITY"
    }

}
