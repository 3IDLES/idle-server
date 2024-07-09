package com.swm.idle.domain.common.exception

import com.swm.idle.support.common.exception.CustomException
import com.swm.idle.support.security.jwt.exception.JwtException.Companion.CODE_PREFIX

sealed class PersistenceException(
    codeNumber: Int,
    message: String,
) : CustomException(CODE_PREFIX, codeNumber, message) {

    class ResourceNotFound(message: String = "") :
        PersistenceException(codeNumber = 1, message = message)

}
