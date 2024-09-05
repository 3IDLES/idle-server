package com.swm.idle.domain.user.common.vo

@JvmInline
value class OfficeNumber(val value: String) {

    init {
        require(value.matches(Regex(VALIDATION_REGEX))) {
            "센터 채용 담당자 전화번호 형식이 올바르지 않습니다."
        }
    }

    companion object {

        const val VALIDATION_REGEX = "^[0-9-]+$"
    }

}
