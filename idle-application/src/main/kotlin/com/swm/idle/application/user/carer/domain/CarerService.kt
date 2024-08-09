package com.swm.idle.application.user.carer.domain

import com.swm.idle.domain.common.exception.PersistenceException
import com.swm.idle.domain.user.carer.entity.jpa.Carer
import com.swm.idle.domain.user.carer.enums.JobSearchStatus
import com.swm.idle.domain.user.carer.repository.jpa.CarerJpaRepository
import com.swm.idle.domain.user.common.enum.GenderType
import com.swm.idle.domain.user.common.vo.BirthYear
import com.swm.idle.domain.user.common.vo.PhoneNumber
import com.swm.idle.support.common.uuid.UuidCreator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Service
class CarerService(
    private val carerJpaRepository: CarerJpaRepository,
) {

    @Transactional
    fun create(
        carerName: String,
        birthYear: BirthYear,
        gender: GenderType,
        phoneNumber: PhoneNumber,
        roadNameAddress: String,
        lotNumberAddress: String,
        longitude: String,
        latitude: String,
    ): Carer {
        return carerJpaRepository.save(
            Carer(
                id = UuidCreator.create(),
                birthYear = birthYear.value,
                carerName = carerName,
                gender = gender,
                phoneNumber = phoneNumber.value,
                roadNameAddress = roadNameAddress,
                lotNumberAddress = lotNumberAddress,
                longitude = BigDecimal(longitude),
                latitude = BigDecimal(latitude),
            )
        )
    }

    fun findByPhoneNumber(phoneNumber: PhoneNumber): Carer? {
        return carerJpaRepository.findByPhoneNumber(phoneNumber.value)
    }

    fun getById(carerId: UUID): Carer {
        return carerJpaRepository.findByIdOrNull(carerId)
            ?: throw PersistenceException.ResourceNotFound("Carer(id=$carerId)를 찾을 수 없습니다")
    }

    fun update(
        carer: Carer,
        experienceYear: Int?,
        roadNameAddress: String,
        lotNumberAddress: String,
        longitude: String,
        latitude: String,
        introduce: String?,
        speciality: String?,
        jobSearchStatus: JobSearchStatus,
    ) {
        carer.update(
            experienceYear = experienceYear,
            roadNameAddress = roadNameAddress,
            lotNumberAddress = lotNumberAddress,
            longitude = BigDecimal(longitude),
            latitude = BigDecimal(latitude),
            introduce = introduce,
            speciality = speciality,
            jobSearchStatus = jobSearchStatus,
        )
    }

    fun updateWithoutAddress(
        carer: Carer,
        experienceYear: Int?,
        introduce: String?,
        speciality: String?,
        jobSearchStatus: JobSearchStatus,
    ) {
        carer.updateWithoutAddress(
            experienceYear = experienceYear,
            introduce = introduce,
            speciality = speciality,
            jobSearchStatus = jobSearchStatus,
        )
    }

    @Transactional
    fun delete(id: UUID) {
        carerJpaRepository.deleteById(id)
    }

}
