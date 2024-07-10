package com.swm.idle.domain.user.repository

import com.swm.idle.domain.user.entity.DeletedUserInfo
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DeletedUserInfoRepository : JpaRepository<DeletedUserInfo, UUID> {
}
