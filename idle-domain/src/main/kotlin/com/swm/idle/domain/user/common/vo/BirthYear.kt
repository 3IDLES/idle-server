package com.swm.idle.domain.user.common.vo

import java.time.LocalDateTime

@JvmInline
value class BirthYear(val value: Int) {

    init {
        require(value in MINIMUM_BIRTH_YEAR..MAXIMUM_BIRTH_YEAR)
    }

    companion object {

        const val MINIMUM_BIRTH_YEAR = 1900
        const val MAXIMUM_BIRTH_YEAR = 2024

        fun calculateAge(value: Int): Int {
            return LocalDateTime.now().year - value + 1
        }
    }

}
