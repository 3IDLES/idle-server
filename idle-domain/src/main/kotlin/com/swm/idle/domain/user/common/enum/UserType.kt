package com.swm.idle.domain.user.common.enum

enum class UserType(val value: String) {
    CENTER("center"),
    CARER("carer");

    companion object {

        fun from(value: String): UserType {
            return entries.find { it.value.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("지원하지 않는 도메인 타입입니다.: $value")
        }

    }

}
