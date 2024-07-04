package com.swm.idle.domain.user.vo

@JvmInline
value class UserSmsVerificationNumber(val value: String) {

    init {
        require(value.toString().length == 6)
    }

}
