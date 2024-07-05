package com.swm.idle.domain.sms.vo

@JvmInline
value class PhoneNumber(val value: String) {

    init {
        require(value.matches(Regex(VALIDATION_REGEX))) {
            "전화번호 형식이 올바르지 않습니다."
        }
    }

    companion object {
        const val VALIDATION_REGEX = "^010-\\d{4}-\\d{4}$"
    }

}
