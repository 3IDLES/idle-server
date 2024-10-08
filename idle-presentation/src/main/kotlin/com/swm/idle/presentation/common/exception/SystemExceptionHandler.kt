package com.swm.idle.presentation.common.exception

import com.swm.idle.support.common.exception.SystemException
import io.github.oshai.kotlinlogging.KotlinLogging
import io.sentry.Sentry
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

    private val logger = KotlinLogging.logger { }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable::class)
    fun handleSystemException(
        exception: Throwable,
        request: HttpServletRequest,
    ): ErrorResponse {
        val requestMethod: String = request.method
        val requestUrl: String = request.requestURI
        val queryString: String = request.queryString?.let { "?$it" } ?: ""
        val clientIp: String = request.getHeader("X-Forwarded-For") ?: request.remoteAddr

        logger.error {
            "$requestMethod $requestUrl$queryString from $clientIp\n" +
                    "[SYSTEM-ERROR] : ${exception.message}"
        }

        Sentry.captureException(exception) { scope ->
            scope.setExtra("requestMethod", requestMethod)
            scope.setExtra("requestUrl", requestUrl)
            scope.setExtra("queryString", queryString)
            scope.setExtra("clientIp", clientIp)
            getRequestBody(request)?.let { scope.setExtra("requestBody", it) }
        }

        return ErrorResponse(
            code = SystemException.InternalServerError().code,
            message = "Internal Server Error"
        )
    }

    private fun getRequestBody(request: HttpServletRequest): String? {
        return runCatching {
            request.inputStream.bufferedReader().use { it.readText() }
        }.getOrNull()
    }

}
