package com.swm.idle.domain.apply.exception

import com.swm.idle.support.common.exception.CustomException

sealed class ApplyException(
    codeNumber: Int,
    message: String,
) : CustomException(CODE_PREFIX, codeNumber, message) {

    class AlreadyApplied(message: String = "") :
        ApplyException(codeNumber = 1, message = message)

    companion object {

        const val CODE_PREFIX = "APPLYMENT"
    }

}
