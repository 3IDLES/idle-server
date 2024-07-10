package com.swm.idle.domain.common.util

import com.swm.idle.domain.user.vo.UserAuthentication
import com.swm.idle.support.security.jwt.context.SecurityContextHolder
import com.swm.idle.support.security.jwt.exception.SecurityException

fun getUserAuthentication(): UserAuthentication {
    val userAuthentication: UserAuthentication? =
        SecurityContextHolder.getContext<UserAuthentication>()
            ?.getAuthentication()

    return userAuthentication ?: run {
        throw SecurityException.UnAuthorizedRequest()
    }

}
