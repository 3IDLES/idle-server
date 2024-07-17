package com.swm.idle.application.user.vo

@JvmInline
value class UserPhoneVerificationNumber(val value: String) {

    init {
        require(value.length == 6) {
            "올바르지 않은 형식의 인증번호입니다."
        }
    }

}
