package com.swm.idle.domain.user.vo

import com.swm.idle.domain.sms.vo.PhoneNumber
import com.swm.idle.support.security.jwt.common.Authentication
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