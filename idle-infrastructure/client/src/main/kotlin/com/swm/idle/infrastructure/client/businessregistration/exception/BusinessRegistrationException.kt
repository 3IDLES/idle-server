package com.swm.idle.infrastructure.client.businessregistration.exception

import com.swm.idle.support.common.exception.CustomException

sealed class BusinessRegistrationException(
    codeNumber: Int,
    message: String,
) : CustomException(CODE_PREFIX, codeNumber, message) {

    class CompanyNotFound(message: String = "사업자 등록번호 검색 결과가 존재하지 않습니다.") :
        BusinessRegistrationException(codeNumber = 1, message = message)

    companion object {
        const val CODE_PREFIX = "CLIENT"
    }

}
