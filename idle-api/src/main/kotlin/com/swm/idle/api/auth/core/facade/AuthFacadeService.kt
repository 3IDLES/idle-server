package com.swm.idle.api.auth.core.facade

import com.swm.idle.domain.sms.exception.SmsException
import com.swm.idle.domain.sms.service.SmsVerificationService
import com.swm.idle.domain.sms.vo.PhoneNumber
import com.swm.idle.domain.sms.vo.SmsVerificationNumber
import com.swm.idle.infrastructure.sms.auth.service.SmsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthFacadeService(
    private val smsService: SmsService,
    private val smsVerificationService: SmsVerificationService,
) {

    @Transactional
    fun sendVerificationMessage(phoneNumber: PhoneNumber) {
        CoroutineScope(Dispatchers.IO).launch {
            smsService.sendVerificationMessage(
                phoneNumber = phoneNumber
            ).run {
                smsVerificationService.save(
                    phoneNumber = this.phoneNumber,
                    smsVerificationNumber = this.smsVerificationNumber,
                    expireSeconds = this.expireSeconds,
                )
            }
        }
    }

    fun confirmVerificationMessage(
        phoneNumber: PhoneNumber,
        verificationNumber: SmsVerificationNumber,
    ) {
        smsVerificationService.findByPhoneNumber(phoneNumber)?.let {
            if (it.first != phoneNumber || it.second != verificationNumber) {
                throw SmsException.InvalidSmsVerificationNumber()
            }
        } ?: throw SmsException.SmsVerificationNumberNotFound()
    }

}
