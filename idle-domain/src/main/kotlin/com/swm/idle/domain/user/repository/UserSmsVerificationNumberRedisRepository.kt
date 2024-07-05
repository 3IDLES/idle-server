package com.swm.idle.domain.user.repository

import com.swm.idle.domain.user.entity.UserSmsVerificationNumberRedisHash
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserSmsVerificationNumberRedisRepository :
    CrudRepository<UserSmsVerificationNumberRedisHash, String>
