package com.swm.idle.support.common.exception

sealed class SystemException(
    codeNumber: Int,
    message: String,
) : CustomException(CODE_PREFIX, codeNumber, message) {

    class InternalServerError(message: String = "") :
        SystemException(codeNumber = 1, message = message)

    companion object {
        const val CODE_PREFIX = "SYSTEM"
    }

}
