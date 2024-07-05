package com.swm.idle.domain.sms.repository

import com.swm.idle.domain.sms.entity.SmsVerificationNumberRedisHash
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SmsVerificationNumberRedisRepository :
    CrudRepository<SmsVerificationNumberRedisHash, String>
