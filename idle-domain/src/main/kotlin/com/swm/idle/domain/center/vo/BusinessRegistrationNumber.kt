package com.swm.idle.domain.center.vo

@JvmInline
value class BusinessRegistrationNumber(val value: String) {

    init {
        require(value.matches(Regex(VALIDATION_REGEX)))
    }

    companion object {
        const val VALIDATION_REGEX = "^\\d{3}-\\d{2}-\\d{5}$"
    }
}
