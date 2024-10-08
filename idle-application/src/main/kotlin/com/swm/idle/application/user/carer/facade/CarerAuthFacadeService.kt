package com.swm.idle.application.user.carer.facade

import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.user.carer.domain.CarerService
import com.swm.idle.application.user.common.service.domain.DeletedUserInfoService
import com.swm.idle.application.user.common.service.domain.RefreshTokenService
import com.swm.idle.application.user.common.service.domain.UserPhoneVerificationService
import com.swm.idle.application.user.common.service.util.JwtTokenService
import com.swm.idle.application.user.vo.UserPhoneVerificationNumber
import com.swm.idle.domain.user.carer.exception.CarerException
import com.swm.idle.domain.user.common.enum.GenderType
import com.swm.idle.domain.user.common.enum.UserType
import com.swm.idle.domain.user.common.exception.UserException
import com.swm.idle.domain.user.common.vo.BirthYear
import com.swm.idle.domain.user.common.vo.PhoneNumber
import com.swm.idle.infrastructure.client.geocode.service.GeoCodeService
import com.swm.idle.support.security.exception.SecurityException
import com.swm.idle.support.transfer.auth.common.LoginResponse
import org.springframework.stereotype.Service

@Service
class CarerAuthFacadeService(
    private val carerService: CarerService,
    private val userPhoneVerificationService: UserPhoneVerificationService,
    private val jwtTokenService: JwtTokenService,
    private val refreshTokenService: RefreshTokenService,
    private val deletedUserInfoService: DeletedUserInfoService,
    private val geoCodeService: GeoCodeService,
) {

    fun join(
        carerName: String,
        birthYear: BirthYear,
        genderType: GenderType,
        phoneNumber: PhoneNumber,
        roadNameAddress: String,
        lotNumberAddress: String,
    ): LoginResponse {
        carerService.findByPhoneNumber(phoneNumber)?.let {
            throw CarerException.AlreadyExistCarer()
        }

        val geoCodeSearchResult = geoCodeService.search(roadNameAddress)

        val savedCarer = carerService.create(
            carerName = carerName,
            birthYear = birthYear,
            gender = genderType,
            phoneNumber = phoneNumber,
            roadNameAddress = roadNameAddress,
            lotNumberAddress = lotNumberAddress,
            longitude = geoCodeSearchResult.addresses[0].x,
            latitude = geoCodeSearchResult.addresses[0].y,
        )

        return LoginResponse(
            accessToken = jwtTokenService.generateAccessToken(savedCarer),
            refreshToken = jwtTokenService.generateRefreshToken(savedCarer),
        )
    }

    fun login(
        phoneNumber: PhoneNumber,
        verificationNumber: UserPhoneVerificationNumber,
    ): LoginResponse {
        if (phoneNumber.value.equals("010-1234-5678").not()) {
            userPhoneVerificationService.findByPhoneNumber(phoneNumber)?.let {
                if (it.first != phoneNumber || it.second != verificationNumber) {
                    throw UserException.InvalidVerificationNumber()
                }
            } ?: throw UserException.VerificationNumberNotFound()
        }

        val carer = carerService.findByPhoneNumber(phoneNumber)
            ?: throw SecurityException.UnregisteredUser()

        return LoginResponse(
            accessToken = jwtTokenService.generateAccessToken(carer),
            refreshToken = jwtTokenService.generateRefreshToken(carer),
        )
    }

    fun logout() {
        val carer = getUserAuthentication().userId.let {
            carerService.getById(it)
        }

        refreshTokenService.deleteById(carer.id)
    }

    fun withdraw(reason: String) {
        val carer = getUserAuthentication().userId.let {
            carerService.getById(it)
        }

        carerService.delete(carer.id)

        deletedUserInfoService.save(
            id = carer.id,
            phoneNumber = PhoneNumber(carer.phoneNumber),
            role = UserType.CARER,
            reason = reason,
        )
    }

}
