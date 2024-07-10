package com.swm.idle.api.auth.center.facade

import com.swm.idle.api.auth.center.dto.LoginResponse
import com.swm.idle.api.auth.center.dto.RefreshLoginTokenResponse
import com.swm.idle.api.auth.center.dto.ValidateBusinessRegistrationNumberResponse
import com.swm.idle.domain.center.service.CenterManagerService
import com.swm.idle.domain.center.vo.BusinessRegistrationNumber
import com.swm.idle.domain.center.vo.Identifier
import com.swm.idle.domain.center.vo.Password
import com.swm.idle.domain.common.exception.PersistenceException
import com.swm.idle.domain.common.util.getUserAuthentication
import com.swm.idle.domain.sms.vo.PhoneNumber
import com.swm.idle.domain.user.service.RefreshTokenService
import com.swm.idle.domain.user.util.JwtTokenService
import com.swm.idle.infrastructure.client.center.service.CenterAuthClientService
import com.swm.idle.infrastructure.client.common.exception.ClientException
import com.swm.idle.support.common.encrypt.PasswordEncryptor
import org.springframework.stereotype.Service

@Service
class CenterAuthFacadeService(
    private val centerManagerService: CenterManagerService,
    private val centerAuthClientService: CenterAuthClientService,
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
        } ?: throw CenterException.ManagerNotFound()

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

}
