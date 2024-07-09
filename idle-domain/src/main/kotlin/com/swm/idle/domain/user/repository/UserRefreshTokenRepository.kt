package com.swm.idle.domain.user.repository

import com.swm.idle.domain.user.entity.UserRefreshTokenRedisHash
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRefreshTokenRepository : CrudRepository<UserRefreshTokenRedisHash, UUID> {
}
