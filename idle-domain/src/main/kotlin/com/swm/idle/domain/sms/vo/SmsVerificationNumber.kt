package com.swm.idle.domain.sms.vo

@JvmInline
value class SmsVerificationNumber(val value: String) {

    init {
        require(value.toString().length == 6)
    }

}
