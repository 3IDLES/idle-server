package com.swm.idle.support.security.jwt.exception

sealed class JwtException(
    val code: String,
    override val message: String,
) : RuntimeException(message) {

    class TokenDecodeException(message: String = "유효하지 않은 토큰입니다.") :
        JwtException(code = "JWT-001", message = message)

    class TokenNotValid(message: String = "유효하지 않은 토큰입니다.") :
        JwtException(code = "JWT-002", message = message)

    class TokenExpired(message: String = "토큰이 만료되었습니다.") :
        JwtException(code = "JWT-003", message = message)

    class TokenNotFound(message: String = "토큰을 찾을 수 없습니다.") :
        JwtException(code = "JWT-004", message = message)

    companion object {
        const val CODE_PREFIX = "JWT"
    }

}
