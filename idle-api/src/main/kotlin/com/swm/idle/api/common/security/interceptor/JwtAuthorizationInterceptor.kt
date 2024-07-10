package com.swm.idle.api.common.security.interceptor

import com.swm.idle.api.common.security.annotation.Secured
import com.swm.idle.domain.user.vo.UserAuthentication
import com.swm.idle.support.security.context.SecurityContextHolder
import com.swm.idle.support.security.exception.SecurityException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class JwtAuthorizationInterceptor : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (handler !is HandlerMethod) {
            return super.preHandle(request, response, handler)
        }

        if (hasSecuredAnnotation(handler) && isNotAuthenticated()) {
            throw SecurityException.UnAuthorizedRequest()
        }

        return super.preHandle(request, response, handler)
    }

    private fun hasSecuredAnnotation(handler: Any): Boolean {
        return (handler as HandlerMethod).hasMethodAnnotation(Secured::class.java)
    }

    private fun isNotAuthenticated(): Boolean {
        return SecurityContextHolder
            .getContext<UserAuthentication>()
            .let { it == null }
    }

}
