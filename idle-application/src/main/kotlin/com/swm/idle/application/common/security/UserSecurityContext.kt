package com.swm.idle.application.common.security

import com.swm.idle.application.user.vo.UserAuthentication
import com.swm.idle.support.security.context.SecurityContext

class UserSecurityContext(
    private var userAuthentication: UserAuthentication,
) : SecurityContext<UserAuthentication> {

    override fun getAuthentication(): UserAuthentication {
        return userAuthentication
    }

    override fun setAuthentication(authentication: UserAuthentication) {
        this.userAuthentication = authentication
    }

}
