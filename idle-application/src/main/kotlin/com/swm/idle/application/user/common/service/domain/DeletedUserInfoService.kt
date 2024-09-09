package com.swm.idle.application.user.common.service.domain

import com.swm.idle.domain.user.common.entity.jpa.DeletedUserInfo
import com.swm.idle.domain.user.common.enum.UserType
import com.swm.idle.domain.user.common.repository.jpa.DeletedUserInfoJpaRepository
import com.swm.idle.domain.user.common.vo.PhoneNumber
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
@Transactional(readOnly = true)
class DeletedUserInfoService(
    private val deletedUserInfoJpaRepository: DeletedUserInfoJpaRepository,
) {

    @Transactional
    fun save(
        id: UUID,
        phoneNumber: PhoneNumber,
        role: UserType,
        reason: String,
    ) {
        deletedUserInfoJpaRepository.save(
            DeletedUserInfo(
                id = id,
                phoneNumber = phoneNumber.value,
                role = role,
                reason = reason,
                deletedAt = LocalDateTime.now(),
            )
        )
    }

    fun findById(carerId: UUID): DeletedUserInfo? {
        return deletedUserInfoJpaRepository.findById(carerId).getOrNull()
    }

}
