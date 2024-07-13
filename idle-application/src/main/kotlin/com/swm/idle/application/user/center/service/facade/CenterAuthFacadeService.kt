package com.swm.idle.application.user.center.service.facade

import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.user.center.service.domain.CenterManagerService
import com.swm.idle.application.user.common.service.domain.DeletedUserInfoService
import com.swm.idle.application.user.common.service.domain.RefreshTokenService
import com.swm.idle.application.user.common.service.util.JwtTokenService
import com.swm.idle.domain.common.exception.PersistenceException
import com.swm.idle.domain.user.center.exception.CenterException
import com.swm.idle.domain.user.center.vo.BusinessRegistrationNumber
import com.swm.idle.domain.user.center.vo.Identifier
import com.swm.idle.domain.user.center.vo.Password
import com.swm.idle.domain.user.common.enum.UserRoleType
import com.swm.idle.domain.user.common.vo.PhoneNumber
import com.swm.idle.infrastructure.client.center.service.CenterAuthClientService
import com.swm.idle.infrastructure.client.common.exception.ClientException

import com.swm.idle.support.common.encrypt.PasswordEncryptor
import com.swm.idle.support.mapper.auth.center.LoginResponse
import com.swm.idle.support.mapper.auth.center.RefreshLoginTokenResponse
import com.swm.idle.support.mapper.auth.center.ValidateBusinessRegistrationNumberResponse
import com.swm.idle.support.security.exception.SecurityException
import org.springframework.stereotype.Service

@Service
class CenterAuthFacadeService(
    private val centerManagerService: CenterManagerService,
    private val centerAuthClientService: CenterAuthClientService,
    private val deletedUserInfoService: DeletedUserInfoService,
    private val jwtTokenService: JwtTokenService,
    private val refreshTokenService: RefreshTokenService,
) {

    fun join(
        identifier: Identifier,
        password: Password,
        phoneNumber: PhoneNumber,
        managerName: String,
        centerBusinessRegistrationNumber: BusinessRegistrationNumber,
    ) {
        centerManagerService.findByPhoneNumber(phoneNumber)?.let {
            throw CenterException.AlreadyExistUser()
        }

        centerManagerService.save(
            identifier = identifier,
            password = password,
            phoneNumber = phoneNumber,
            managerName = managerName,
            centerBusinessRegistrationNumber = centerBusinessRegistrationNumber,
        )
    }

    fun validateCompany(businessRegistrationNumber: BusinessRegistrationNumber): ValidateBusinessRegistrationNumberResponse {
        val result =
            centerAuthClientService.sendCompanyValidationRequest(businessRegistrationNumber)

        val item = result.items.firstOrNull()
            ?: throw ClientException.CompanyNotFoundException()

        return ValidateBusinessRegistrationNumberResponse(
            businessRegistrationNumber = item.bno,
            companyName = item.company,
        )
    }

    fun login(
        identifier: Identifier,
        password: Password,
    ): LoginResponse {
        val centerManager = centerManagerService.findByIdentifier(identifier)?.takeIf {
            PasswordEncryptor.matchPassword(password.value, it.password)
        } ?: throw SecurityException.InvalidLoginRequest()

        return LoginResponse(
            accessToken = jwtTokenService.generateAccessToken(centerManager),
            refreshToken = jwtTokenService.generateRefreshToken(centerManager),
        )
    }

    fun refreshLoginToken(refreshToken: String): RefreshLoginTokenResponse {
        return refreshTokenService.create(refreshToken)
            .let {
                RefreshLoginTokenResponse(
                    accessToken = it.accessToken,
                    refreshToken = it.refreshToken,
                )
            }
    }

    fun validateIdentifier(identifier: Identifier) {
        centerManagerService.validateDuplicateIdentifier(identifier)
    }

    fun logout() {
        val centerManagerId = getUserAuthentication().userId

        if (centerManagerService.existsById(centerManagerId).not()) {
            throw PersistenceException.ResourceNotFound("센터 관리자(id: $centerManagerId)를 찾을 수 없습니다.")
        }

        refreshTokenService.delete(
            userId = centerManagerId,
        )
    }

    fun withDraw(
        reason: String,
        password: Password,
    ) {
        val centerManagerId = getUserAuthentication().userId
        val centerManager = centerManagerService.getById(centerManagerId)

        if (PasswordEncryptor.matchPassword(password.value, centerManager.password).not()) {
            throw SecurityException.InvalidPassword()
        }

        deletedUserInfoService.save(
            id = centerManagerId,
            phoneNumber = PhoneNumber(centerManager.phoneNumber),
            role = UserRoleType.CENTER_MANAGER,
            reason = reason,
        )

        centerManagerService.delete(centerManagerId)
    }

}
