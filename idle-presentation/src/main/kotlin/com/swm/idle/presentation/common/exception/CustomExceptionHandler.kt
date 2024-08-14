package com.swm.idle.presentation.common.exception

import com.swm.idle.support.common.exception.CustomException
import com.swm.idle.support.security.exception.JwtException
import com.swm.idle.support.security.exception.SecurityException
import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Hidden
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@Hidden
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class CustomExceptionHandler {

    private val logger = KotlinLogging.logger { }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(
        exception: CustomException,
        request: HttpServletRequest,
    ): ErrorResponse {
        val requestMethod: String = request.method
        val requestUrl: String = request.requestURI
        val queryString: String = request.queryString?.let { "?$it" } ?: ""
        val clientIp: String = request.getHeader("X-Forwarded-For") ?: request.remoteAddr

        logger.warn {
            "$requestMethod $requestUrl$queryString from $clientIp\n" +
                    "[${exception.code}] : ${exception.message}"
        }

        return ErrorResponse(
            code = exception.code,
            message = exception.message,
        )
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtException::class)
    fun handlerJwtException(
        exception: JwtException,
        request: HttpServletRequest,
    ): ErrorResponse {
        val requestMethod: String = request.method
        val requestUrl: String = request.requestURI
        val queryString: String = request.queryString?.let { "?$it" } ?: ""
        val clientIp: String = request.getHeader("X-Forwarded-For") ?: request.remoteAddr

        logger.warn {
            "$requestMethod $requestUrl$queryString from $clientIp\n" +
                    "[${exception.code}] : ${exception.message}"
        }


        return ErrorResponse(
            code = exception.code,
            message = exception.message,
        )
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(SecurityException::class)
    fun handlerSecurityException(
        exception: SecurityException,
        request: HttpServletRequest,

        ): ErrorResponse {
        val requestMethod: String = request.method
        val requestUrl: String = request.requestURI
        val queryString: String = request.queryString?.let { "?$it" } ?: ""
        val clientIp: String = request.getHeader("X-Forwarded-For") ?: request.remoteAddr

        logger.warn {
            "$requestMethod $requestUrl$queryString from $clientIp\n" +
                    "[${exception.code}] : ${exception.message}"
        }

        return ErrorResponse(
            code = exception.code,
            message = exception.message,
        )
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        exception: IllegalArgumentException,
        request: HttpServletRequest,
    ): ErrorResponse {
        val requestMethod: String = request.method
        val requestUrl: String = request.requestURI
        val queryString: String = request.queryString?.let { "?$it" } ?: ""
        val clientIp: String = request.getHeader("X-Forwarded-For") ?: request.remoteAddr

        logger.warn {
            "${exception::class.simpleName}: $requestMethod $requestUrl$queryString from $clientIp\n" +
                    "${exception.message}"
        }

        return ErrorResponse(
            code = ApiException.InvalidParameter().code,
            message = exception.message!!,
        )
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        exception: MethodArgumentNotValidException,
        request: HttpServletRequest,
    ): ErrorResponse {
        val requestMethod: String = request.method
        val requestUrl: String = request.requestURI
        val queryString: String = request.queryString?.let { "?$it" } ?: ""
        val clientIp: String = request.getHeader("X-Forwarded-For") ?: request.remoteAddr

        val builder = StringBuilder()
        exception
            .bindingResult
            .fieldErrors
            .forEach { fieldError ->
                builder
                    .append("[${fieldError.field}](은)는 ${fieldError.defaultMessage}")
                    .append("입력된 값: ${fieldError.rejectedValue}")
                    .append("|")
            }
        val message = builder.toString()

        logger.warn {
            "$requestMethod $requestUrl$queryString from $clientIp\n" +
                    exception.message
        }

        return ErrorResponse(
            code = ApiException.InvalidParameter().code,
            message = message
        )
    }

}
