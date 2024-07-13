package com.swm.idle.application.user.vo

import com.swm.idle.domain.user.common.vo.PhoneNumber
import com.swm.idle.support.security.common.Authentication
import java.util.*

data class UserAuthentication(
    val userId: UUID,
    val phoneNumber: PhoneNumber,
) : Authentication {

    companion object {
        fun from(claims: UserTokenClaims.AccessToken): UserAuthentication {
            return UserAuthentication(
                userId = claims.userId,
                phoneNumber = claims.phoneNumber,
            )
        }
    }

    override fun getName(): String {
        return userId.toString()
    }

}
