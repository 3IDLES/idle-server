package com.swm.idle.infrastructure.sms.common.vo

import com.swm.idle.domain.user.vo.PhoneNumber
import com.swm.idle.domain.user.vo.UserSmsVerificationNumber

data class SmsVerificationInfo(
    val userSmsVerificationNumber: UserSmsVerificationNumber,
    val phoneNumber: PhoneNumber,
    val expireSeconds: Long,
)
