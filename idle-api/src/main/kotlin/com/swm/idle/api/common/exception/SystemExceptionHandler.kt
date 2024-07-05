package com.swm.idle.api.common.exception

import com.swm.idle.support.common.exception.SystemException
import io.swagger.v3.oas.annotations.Hidden
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@Hidden
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE + 1)
class SystemExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(SystemException::class)
    fun handleSystemException(
        e: Throwable,
        request: HttpServletRequest,
    ): ErrorResponse {
        return ErrorResponse(
            code = SystemException.InternalServerError().code,
            message = "Internal Server Error"
        )
    }

}
