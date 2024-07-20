package com.swm.idle.domain.user.center.exception

import com.swm.idle.support.common.exception.CustomException

sealed class CenterException(
    codeNumber: Int,
    message: String,
) : CustomException(CODE_PREFIX, codeNumber, message) {

    class DuplicateIdentifier(message: String = "이미 존재하는 ID입니다.") :
        CenterException(codeNumber = 1, message = message)

    class AlreadyExistCenterManager(message: String = "이미 가입된 센터 관리자입니다.") :
        CenterException(codeNumber = 2, message = message)

    class AlreadyExistCenter(message: String = "등록된 센터 정보가 이미 존재합니다.") :
        CenterException(codeNumber = 3, message = message)

    class NotFoundException(message: String = "존재하지 않는 센터입니다.") :
        CenterException(codeNumber = 4, message = message)

    companion object {

        const val CODE_PREFIX = "CENTER"
    }

}
