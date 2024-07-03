package com.swm.idle.domain.user.vo

@JvmInline
value class SmsVerificationNumber(val value: Int) {

    init {
        require(value.toString().length == 6)
    }

}
