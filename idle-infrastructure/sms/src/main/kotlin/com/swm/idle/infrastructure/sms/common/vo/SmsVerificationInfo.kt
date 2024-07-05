package com.swm.idle.infrastructure.sms.common.vo

import com.swm.idle.domain.sms.vo.PhoneNumber
import com.swm.idle.domain.sms.vo.SmsVerificationNumber

data class SmsVerificationInfo(
    val smsVerificationNumber: SmsVerificationNumber,
    val phoneNumber: PhoneNumber,
    val expireSeconds: Long,
)
