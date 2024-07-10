package com.swm.idle.support.security.exception

import com.swm.idle.support.common.exception.CustomException

sealed class JwtException(
    codeNumber: Int,
    message: String,
) : CustomException(CODE_PREFIX, codeNumber, message) {

    class TokenDecodeException(message: String = "유효하지 않은 토큰입니다.") :
        JwtException(codeNumber = 1, message = message)

    class TokenNotValid(message: String = "유효하지 않은 토큰입니다.") :
        JwtException(codeNumber = 2, message = message)

    class TokenExpired(message: String = "토큰이 만료되었습니다.") :
        JwtException(codeNumber = 3, message = message)

    class TokenNotFound(message: String = "토큰을 찾을 수 없습니다.") :
        JwtException(codeNumber = 4, message = message)

    companion object {
        const val CODE_PREFIX = "JWT"
    }

}
