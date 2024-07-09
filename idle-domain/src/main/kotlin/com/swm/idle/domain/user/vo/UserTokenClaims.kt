package com.swm.idle.domain.user.vo

import com.swm.idle.domain.sms.vo.PhoneNumber
import java.util.*

sealed class UserTokenClaims {

    data class AccessToken(
        val userId: UUID,
        val phoneNumber: PhoneNumber,
    ): UserTokenClaims()

    data class RefreshToken(
        val userId: UUID,
    ): UserTokenClaims()

}
