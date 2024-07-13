package com.swm.idle.domain.user.common.repository.jpa

import com.swm.idle.domain.user.common.entity.jpa.DeletedUserInfo
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DeletedUserInfoRepository : JpaRepository<DeletedUserInfo, UUID> {
}
