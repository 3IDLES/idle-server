package com.swm.idle.api.common.security.context

import com.swm.idle.domain.user.vo.UserAuthentication
import com.swm.idle.support.security.jwt.context.SecurityContext

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
