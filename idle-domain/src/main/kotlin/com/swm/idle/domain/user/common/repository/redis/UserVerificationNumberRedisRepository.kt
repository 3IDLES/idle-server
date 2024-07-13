package com.swm.idle.domain.user.common.repository.redis

import com.swm.idle.domain.user.common.entity.redis.UserVerificationNumberRedisHash
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserVerificationNumberRedisRepository :
    CrudRepository<UserVerificationNumberRedisHash, String>
