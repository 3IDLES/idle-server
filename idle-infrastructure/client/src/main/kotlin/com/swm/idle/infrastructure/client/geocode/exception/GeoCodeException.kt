package com.swm.idle.infrastructure.client.geocode.exception

import com.swm.idle.support.common.exception.CustomException

sealed class GeoCodeException(
    codeNumber: Int,
    message: String,
) : CustomException(CODE_PREFIX, codeNumber, message) {

    class ResultNotFound(message: String = "입력한 주소로 GeoCode 검색 결과가 존재하지 않습니다.") :
        GeoCodeException(codeNumber = 1, message = message)

    companion object {

        const val CODE_PREFIX = "GEOCODE"
    }

}

