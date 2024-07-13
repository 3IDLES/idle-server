package com.swm.idle.application.common.security

import com.swm.idle.application.user.vo.UserAuthentication
import com.swm.idle.support.security.context.SecurityContextHolder
import com.swm.idle.support.security.exception.SecurityException

fun getUserAuthentication(): UserAuthentication {
    val userAuthentication: UserAuthentication? =
        SecurityContextHolder.getContext<UserAuthentication>()
            ?.getAuthentication()

    return userAuthentication ?: run {
        throw SecurityException.UnAuthorizedRequest()
    }

}
