package com.swm.idle.application.user.vo

@JvmInline
value class UserPhoneVerificationNumber(val value: String) {

    init {
        require(value.length == 6)
    }

}
