package com.swm.idle.application.user.center.service.domain

import com.swm.idle.domain.common.exception.PersistenceException
import com.swm.idle.domain.user.center.entity.jpa.CenterManager
import com.swm.idle.domain.user.center.enums.CenterManagerAccountStatus
import com.swm.idle.domain.user.center.exception.CenterException
import com.swm.idle.domain.user.center.repository.jpa.CenterManagerJpaRepository
import com.swm.idle.domain.user.center.vo.BusinessRegistrationNumber
import com.swm.idle.domain.user.center.vo.Identifier
import com.swm.idle.domain.user.center.vo.Password
import com.swm.idle.domain.user.common.vo.PhoneNumber
import com.swm.idle.support.common.encrypt.PasswordEncryptor
import com.swm.idle.support.common.uuid.UuidCreator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CenterManagerService(
    private val centerManagerJpaRepository: CenterManagerJpaRepository,
) {

    @Transactional
    fun save(
        identifier: Identifier,
        password: Password,
        phoneNumber: PhoneNumber,
        managerName: String,
        centerBusinessRegistrationNumber: BusinessRegistrationNumber,
    ) {
        val encryptedPassword = PasswordEncryptor.encrypt(rawPassword = password.value)

        CenterManager(
            id = UuidCreator.create(),
            identifier = identifier.value,
            password = encryptedPassword,
            phoneNumber = phoneNumber.value,
            managerName = managerName,
            status = CenterManagerAccountStatus.PENDING,
            centerBusinessRegistrationNumber = centerBusinessRegistrationNumber.value,
        ).also {
            centerManagerJpaRepository.save(it)
        }
    }

    fun findByIdentifier(identifier: Identifier): CenterManager? {
        return centerManagerJpaRepository.findByIdentifier(identifier.value)
    }

    fun getById(id: UUID): CenterManager {
        return centerManagerJpaRepository
            .findById(id)
            .orElseThrow { PersistenceException.ResourceNotFound("유저(id: $id)를 찾을 수 없습니다.") }
    }

    fun validateDuplicateIdentifier(identifier: Identifier) {
        if (centerManagerJpaRepository.existsByIdentifier(identifier.value)) {
            throw CenterException.DuplicateIdentifier()
        }
    }

    fun existsById(centerManagerId: UUID): Boolean {
        return centerManagerJpaRepository.existsById(centerManagerId)
    }

    @Transactional
    fun delete(centerManagerId: UUID) {
        centerManagerJpaRepository.deleteById(centerManagerId)
    }

    fun findByPhoneNumber(phoneNumber: PhoneNumber): CenterManager? {
        return centerManagerJpaRepository.findByPhoneNumber(phoneNumber.value)
    }

}
