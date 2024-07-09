package com.swm.idle.support.security.jwt.context

import com.swm.idle.support.security.jwt.common.Authentication

interface SecurityContext<T : Authentication> {

    fun getAuthentication(): T

    fun setAuthentication(authentication: T)

}
