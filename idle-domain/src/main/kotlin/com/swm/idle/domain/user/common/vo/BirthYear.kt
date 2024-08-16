package com.swm.idle.domain.user.common.vo

import java.time.LocalDateTime

@JvmInline
value class BirthYear(val value: Int) {

    init {
        require(value in MINIMUM_BIRTH_YEAR..MAXIMUM_BIRTH_YEAR) {
            "출생연도는 $MINIMUM_BIRTH_YEAR ~ $MAXIMUM_BIRTH_YEAR 년 사이의 연도만 입력할 수 있습니다."
        }
    }

    companion object {

        const val MINIMUM_BIRTH_YEAR = 1900
        const val MAXIMUM_BIRTH_YEAR = 2024

        fun calculateAge(value: Int): Int {
            return LocalDateTime.now().year - value + 1
        }
    }

}
