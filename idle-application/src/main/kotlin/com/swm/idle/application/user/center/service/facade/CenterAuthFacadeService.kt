package com.swm.idle.application.user.center.service.facade

import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.user.center.service.domain.CenterManagerService
import com.swm.idle.application.user.common.service.domain.DeletedUserInfoService
import com.swm.idle.application.user.common.service.domain.RefreshTokenService
import com.swm.idle.application.user.common.service.util.JwtTokenService
import com.swm.idle.domain.common.enums.EntityStatus
import com.swm.idle.domain.common.exception.PersistenceException
import com.swm.idle.domain.user.center.exception.CenterException
import com.swm.idle.domain.user.center.vo.BusinessRegistrationNumber
import com.swm.idle.domain.user.center.vo.Identifier
import com.swm.idle.domain.user.center.vo.Password
import com.swm.idle.domain.user.common.enum.UserType
import com.swm.idle.domain.user.common.vo.PhoneNumber
import com.swm.idle.infrastructure.client.businessregistration.exception.BusinessRegistrationException
import com.swm.idle.infrastructure.client.businessregistration.service.BusinessRegistrationNumberValidationService
import com.swm.idle.support.common.encrypt.PasswordEncryptor
import com.swm.idle.support.security.exception.SecurityException
import com.swm.idle.support.transfer.auth.center.ValidateBusinessRegistrationNumberResponse
import com.swm.idle.support.transfer.auth.common.LoginResponse
import com.swm.idle.support.transfer.user.center.JoinStatusInfoResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CenterAuthFacadeService(
    private val centerManagerService: CenterManagerService,
    private val businessRegistrationNumberValidationService: BusinessRegistrationNumberValidationService,
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
            throw CenterException.AlreadyExistCenterManager()
        }

        centerManagerService.save(
            identifier = identifier,
            password = password,
            phoneNumber = phoneNumber,
            managerName = managerName,
            centerBusinessRegistrationNumber = centerBusinessRegistrationNumber,
        )
    }

    fun getCenterManagerJoinStatusInfo(): JoinStatusInfoResponse {
        val centerManager = getUserAuthentication().userId.let {
            centerManagerService.getById(it)
        }

        return JoinStatusInfoResponse.from(centerManager)
    }

    fun validateCompany(businessRegistrationNumber: BusinessRegistrationNumber): ValidateBusinessRegistrationNumberResponse {
        val result =
            businessRegistrationNumberValidationService.sendCompanyValidationRequest(
                businessRegistrationNumber
            )

        val item = result.items.firstOrNull()
            ?: throw BusinessRegistrationException.CompanyNotFound()

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

    fun validateIdentifier(identifier: Identifier) {
        centerManagerService.validateDuplicateIdentifier(identifier)
    }

    fun logout() {
        val centerManagerId = getUserAuthentication().userId

        if (centerManagerService.existsById(centerManagerId).not()) {
            throw PersistenceException.ResourceNotFound("센터 관리자(id: $centerManagerId)를 찾을 수 없습니다.")
        }

        refreshTokenService.deleteById(
            userId = centerManagerId,
        )
    }

    fun withdraw(
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
            role = UserType.CENTER,
            reason = reason,
        )

        centerManagerService.delete(centerManagerId)
    }

    @Transactional
    fun changePassword(
        phoneNumber: PhoneNumber,
        newPassword: Password,
    ) {
        val centerManager = centerManagerService.getByPhoneNumber(phoneNumber)

        centerManagerService.updatePassword(centerManager, newPassword)
    }

    @Transactional
    fun requestCenterManagerVerification() {
        getUserAuthentication().userId
            .let { centerManagerService.getById(it) }
            .takeIf { it.entityStatus == EntityStatus.ACTIVE }
            ?.let { centerManagerService.updateAccountStatusToPending(it) }
    }

}
