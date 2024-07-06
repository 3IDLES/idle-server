package com.swm.idle.domain.center.service

import com.swm.idle.domain.center.entity.CenterManager
import com.swm.idle.domain.center.enums.CenterAccountStatus
import com.swm.idle.domain.center.repository.CenterManagerJpaRepository
import com.swm.idle.domain.center.vo.BusinessRegistrationNumber
import com.swm.idle.domain.sms.vo.PhoneNumber
import com.swm.idle.support.common.uuid.UuidCreator
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CenterManagerService(
    private val centerManagerJpaRepository: CenterManagerJpaRepository,
) {

    @Transactional
    fun save(
        identifier: String,
        password: String,
        phoneNumber: PhoneNumber,
        managerName: String,
        centerBusinessRegistrationNumber: BusinessRegistrationNumber,
    ) {
        CenterManager(
            id = UuidCreator.create(),
            identifier = identifier,
            password = password,
            phoneNumber = phoneNumber.value,
            managerName = managerName,
            status = CenterAccountStatus.PENDING,
            centerBusinessRegistrationNumber = centerBusinessRegistrationNumber.value,
        ).also {
            centerManagerJpaRepository.save(it)
        }
    }

}
