package com.swm.idle.support.common.exception

abstract class CustomException(
    codePrefix: String = DEFAULT_CODE_PREFIX,
    codeNumber: Int,
    override val message: String = DEFAULT_ERROR_MESSAGE,
) : RuntimeException(message) {

    val code: String = "$codePrefix-${
        codeNumber.toString().padStart(DEFAULT_CODE_LENGTH, DEFAULT_CODE_PAD_CHAR)
    }"

    companion object {
        const val DEFAULT_CODE_LENGTH = 3
        const val DEFAULT_CODE_PAD_CHAR = '0'
        const val DEFAULT_CODE_PREFIX = "UNKNOWN"
        const val DEFAULT_ERROR_MESSAGE = "알 수 없는 오류가 발생하였습니다."
    }
}
