package com.swm.idle.infrastructure.client.common.exception

import com.swm.idle.support.common.exception.CustomException

sealed class ClientException(
    codeNumber: Int,
    message: String,
) : CustomException(CODE_PREFIX, codeNumber, message) {

    class ExternalApiException(message: String = "외부 API에서 알 수 없는 문제가 발생하였습니다.") :
        ClientException(codeNumber = 1, message = message)

    class CompanyNotFoundException(message: String = "사업자 등록번호 조회 결과가 존재하지 않습니다.") :
        ClientException(codeNumber = 2, message = message)

    companion object {
        const val CODE_PREFIX = "CLIENT"
    }

}
