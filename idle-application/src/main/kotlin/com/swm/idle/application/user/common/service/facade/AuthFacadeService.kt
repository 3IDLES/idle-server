package com.swm.idle.application.user.common.service.facade

import com.swm.idle.application.user.common.service.domain.RefreshTokenService
import com.swm.idle.application.user.common.service.domain.UserPhoneVerificationService
import com.swm.idle.application.user.vo.UserPhoneVerificationNumber
import com.swm.idle.domain.user.common.exception.UserException
import com.swm.idle.domain.user.common.vo.PhoneNumber
import com.swm.idle.infrastructure.sms.auth.service.SmsService
import com.swm.idle.support.transfer.auth.center.RefreshLoginTokenResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthFacadeService(
    private val smsService: SmsService,
    private val userPhoneVerificationService: UserPhoneVerificationService,
    private val refreshTokenService: RefreshTokenService,
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
                throw UserException.InvalidVerificationNumber()
            }
        } ?: throw UserException.VerificationNumberNotFound()
    }

    @Transactional
    fun refreshLoginToken(refreshToken: String): RefreshLoginTokenResponse {
        return refreshTokenService.create(refreshToken)
            .let {
                RefreshLoginTokenResponse(
                    accessToken = it.accessToken,
                    refreshToken = it.refreshToken,
                )
            }
    }

}
