package com.swm.idle.support.security.context

import com.swm.idle.support.security.common.Authentication

object SecurityContextHolder {

    private val securityContextHolder = ThreadLocal<SecurityContext<*>>()

    @Suppress("UNCHECKED_CAST")
    fun <T : Authentication> getContext(): SecurityContext<T>? {
        return securityContextHolder.get() as SecurityContext<T>?
    }

    fun <T : Authentication> setContext(context: SecurityContext<T>) {
        securityContextHolder.set(context)
    }

    fun clearContext() {
        securityContextHolder.remove()
    }

}
