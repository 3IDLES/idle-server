package com.swm.idle.application.user.common.service.facade

import com.swm.idle.application.user.common.service.domain.UserPhoneVerificationService
import com.swm.idle.application.user.vo.UserPhoneVerificationNumber
import com.swm.idle.domain.user.center.exception.SmsException
import com.swm.idle.domain.user.common.vo.PhoneNumber
import com.swm.idle.infrastructure.sms.auth.service.SmsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthFacadeService(
    private val smsService: SmsService,
    private val userPhoneVerificationService: UserPhoneVerificationService,
) {

    @Transactional
    fun sendVerificationMessage(phoneNumber: PhoneNumber) {
        userPhoneVerificationService.findByPhoneNumber(phoneNumber)?.let {
            userPhoneVerificationService.deleteByPhoneNumber(phoneNumber)
        }

        CoroutineScope(Dispatchers.IO).launch {
            smsService.sendVerificationMessage(
                phoneNumber = phoneNumber
            ).run {
                userPhoneVerificationService.save(
                    phoneNumber = this.phoneNumber,
                    userPhoneVerificationNumber = UserPhoneVerificationNumber(this.userPhoneVerificationNumber),
                    expireSeconds = this.expireSeconds,
                )
            }
        }
    }

    fun confirmVerificationMessage(
        phoneNumber: PhoneNumber,
        verificationNumber: UserPhoneVerificationNumber,
    ) {
        userPhoneVerificationService.findByPhoneNumber(phoneNumber)?.let {
            if (it.first != phoneNumber || it.second != verificationNumber) {
                throw SmsException.InvalidSmsVerificationNumber()
            }
        } ?: throw SmsException.SmsVerificationNumberNotFound()
    }

}
