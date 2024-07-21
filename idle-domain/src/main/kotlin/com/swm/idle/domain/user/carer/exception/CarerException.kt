package com.swm.idle.domain.user.carer.exception

import com.swm.idle.support.common.exception.CustomException

sealed class CarerException(
    codeNumber: Int,
    message: String,
) : CustomException(CODE_PREFIX, codeNumber, message) {

    class AlreadyExistCarer(message: String = "해당 정보로 가입된 요양 보호사가 이미 존재합니다.") :
        CarerException(codeNumber = 1, message = message)

    companion object {

        const val CODE_PREFIX = "CARER"
    }

}
