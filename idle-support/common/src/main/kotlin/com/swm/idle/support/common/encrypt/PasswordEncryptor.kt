package com.swm.idle.support.common.encrypt

import org.mindrot.jbcrypt.BCrypt

object PasswordEncryptor {

    fun encrypt(rawPassword: String): String {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt())
    }

    fun matchPassword(
        enteredPassword: String,
        password: String,
    ): Boolean {
        return BCrypt.checkpw(enteredPassword, password)
    }

}
