package com.swm.idle.application.user.common.service.domain

import com.swm.idle.domain.user.common.entity.jpa.DeletedUserInfo
import com.swm.idle.domain.user.common.enum.UserRoleType
import com.swm.idle.domain.user.common.repository.jpa.DeletedUserInfoRepository
import com.swm.idle.domain.user.common.vo.PhoneNumber
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class DeletedUserInfoService(
    private val deletedUserInfoRepository: DeletedUserInfoRepository,
) {

    @Transactional
    fun save(
        id: UUID,
        phoneNumber: PhoneNumber,
        role: UserRoleType,
        reason: String,
    ) {
        deletedUserInfoRepository.save(
            DeletedUserInfo(
                id = id,
                phoneNumber = phoneNumber.value,
                role = role,
                reason = reason,
                deletedAt = LocalDateTime.now(),
            )
        )
    }

}
