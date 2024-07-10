package com.swm.idle.domain.user.service

import com.swm.idle.domain.sms.vo.PhoneNumber
import com.swm.idle.domain.user.common.enum.UserRoleType
import com.swm.idle.domain.user.entity.DeletedUserInfo
import com.swm.idle.domain.user.repository.DeletedUserInfoRepository
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
