package com.swm.idle.domain.user.center.vo

@JvmInline
value class Password(val value: String) {

    init {
        require(value.matches(Regex(VALIDATION_REGEX))) {
            "올바르지 않은 비밀번호 형식입니다."
        }
    }

    companion object {
        const val VALIDATION_REGEX =
            "^(?=.*[A-Za-z])(?=.*\\d)(?!.*(.)\\1\\1)[A-Za-z\\d!@#\$%^&*()-_=+`~]{8,20}\$"
    }

}
