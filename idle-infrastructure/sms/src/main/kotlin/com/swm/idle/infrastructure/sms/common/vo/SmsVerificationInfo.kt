package com.swm.idle.infrastructure.sms.common.vo

import com.swm.idle.domain.user.common.vo.PhoneNumber

data class SmsVerificationInfo(
    val userPhoneVerificationNumber: String,
    val phoneNumber: PhoneNumber,
    val expireSeconds: Long,
)
