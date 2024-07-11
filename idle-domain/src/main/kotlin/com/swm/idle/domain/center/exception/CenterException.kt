package com.swm.idle.domain.center.exception

import com.swm.idle.support.common.exception.CustomException

sealed class CenterException(
    codeNumber: Int,
    message: String,
) : CustomException(CODE_PREFIX, codeNumber, message) {

    class DuplicateIdentifier(message: String = "이미 존재하는 ID입니다.") :
        CenterException(codeNumber = 1, message = message)

    class AlreadyExistUser(message: String = "이미 가입된 회원입니다.") :
        CenterException(codeNumber = 2, message = message)

    companion object {
        const val CODE_PREFIX = "CENTER"
    }

}
