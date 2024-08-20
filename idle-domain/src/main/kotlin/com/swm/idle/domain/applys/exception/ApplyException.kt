package com.swm.idle.domain.applys.exception

import com.swm.idle.support.common.exception.CustomException

sealed class ApplyException(
    codeNumber: Int,
    message: String,
) : CustomException(CODE_PREFIX, codeNumber, message) {

    class AlreadyApplied(message: String = "이미 지원한 이력이 있는 공고입니다.") :
        ApplyException(codeNumber = 1, message = message)

    companion object {

        const val CODE_PREFIX = "APPLY"
    }

}
