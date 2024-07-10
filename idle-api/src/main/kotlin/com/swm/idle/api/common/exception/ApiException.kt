package com.swm.idle.api.common.exception

import com.swm.idle.support.common.exception.CustomException

sealed class ApiException(
    codeNumber: Int,
    message: String,
) : CustomException(CODE_PREFIX, codeNumber, message) {

    class InvalidParameter(message: String = "") :
        ApiException(codeNumber = 1, message = message)

    companion object {
        const val CODE_PREFIX = "API"
    }

}
