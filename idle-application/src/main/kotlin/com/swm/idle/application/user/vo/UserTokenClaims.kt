package com.swm.idle.application.user.vo

import com.swm.idle.domain.user.common.enum.UserType
import com.swm.idle.domain.user.common.vo.PhoneNumber
import java.util.*

sealed class UserTokenClaims {

    data class AccessToken(
        val userId: UUID,
        val phoneNumber: PhoneNumber,
    ) : UserTokenClaims()

    data class RefreshToken(
        val userId: UUID,
        val userType: UserType,
    ) : UserTokenClaims()

}
