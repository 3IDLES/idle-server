package com.swm.idle.domain.user.common.repository.redis

import com.swm.idle.domain.user.common.entity.redis.UserRefreshTokenRedisHash
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRefreshTokenRepository : CrudRepository<UserRefreshTokenRedisHash, UUID> {
}
