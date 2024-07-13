package com.swm.idle.domain.user.center.vo

@JvmInline
value class Identifier(val value: String) {

    init {
        require(value.matches(Regex(VALIDATION_REGEX))) {
            "올바르지 않은 아이디 형식입니다."
        }
    }

    companion object {
        const val VALIDATION_REGEX = "^[A-Za-z0-9]{6,20}\$"
    }

}
