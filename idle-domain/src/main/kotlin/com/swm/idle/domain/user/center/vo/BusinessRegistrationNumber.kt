package com.swm.idle.domain.user.center.vo

@JvmInline
value class BusinessRegistrationNumber(val value: String) {

    init {
        require(value.matches(Regex(VALIDATION_REGEX))) {
            "올바르지 않은 사업자 등록번호입니다."
        }
    }

    companion object {
        const val VALIDATION_REGEX = "^\\d{3}-\\d{2}-\\d{5}$"
    }
}
