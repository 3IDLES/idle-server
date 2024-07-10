package com.swm.idle.support.security.context

import com.swm.idle.support.security.common.Authentication

interface SecurityContext<T : Authentication> {

    fun getAuthentication(): T

    fun setAuthentication(authentication: T)

}
