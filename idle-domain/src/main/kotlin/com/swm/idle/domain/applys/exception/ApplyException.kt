package com.swm.idle.domain.applys.exception

import com.swm.idle.support.common.exception.CustomException

sealed class ApplyException(
    codeNumber: Int,
    message: String,
) : CustomException(CODE_PREFIX, codeNumber, message) {

    class AlreadyApplied(message: String = "이미 같은 방법으로 지원한 이력이 존재합니다.") :
        ApplyException(codeNumber = 1, message = message)

    companion object {

        const val CODE_PREFIX = "APPLY"
    }

}
