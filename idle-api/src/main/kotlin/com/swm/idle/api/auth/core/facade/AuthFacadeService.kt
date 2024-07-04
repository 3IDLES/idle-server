package com.swm.idle.api.auth.core.facade

import com.swm.idle.domain.user.service.UserSmsVerificationService
import com.swm.idle.domain.user.vo.PhoneNumber
import com.swm.idle.infrastructure.sms.auth.service.SmsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service

@Service
class AuthFacadeService(
    private val smsService: SmsService,
    private val userSmsVerificationService: UserSmsVerificationService,
) {

    fun sendVerificationMessage(phoneNumber: PhoneNumber) {
        CoroutineScope(Dispatchers.IO).launch {
            smsService.sendVerificationMessage(
                phoneNumber = phoneNumber
            ).run {
                userSmsVerificationService.save(
                    phoneNumber = this.phoneNumber,
                    userSmsVerificationNumber = this.userSmsVerificationNumber,
                    expireSeconds = this.expireSeconds,
                )
            }
        }
    }

}
