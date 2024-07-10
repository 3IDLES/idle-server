package com.swm.idle.api.common.exception

import com.swm.idle.support.common.exception.CustomException
import com.swm.idle.support.security.jwt.exception.JwtException
import io.swagger.v3.oas.annotations.Hidden
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(exception: CustomException): ErrorResponse {
        return ErrorResponse(
            code = exception.code,
            message = exception.message,
        )
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtException::class)
    fun handlerJwtException(exception: JwtException): ErrorResponse {
        return ErrorResponse(
            code = exception.code,
            message = exception.message,
        )
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(exception: IllegalArgumentException): ErrorResponse {
        return ErrorResponse(
            code = ApiException.InvalidParameter().code,
            message = exception.message!!,
        )
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException): ErrorResponse {
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
        return ErrorResponse(
            code = ApiException.InvalidParameter().code,
            message = message
        )
    }

}
