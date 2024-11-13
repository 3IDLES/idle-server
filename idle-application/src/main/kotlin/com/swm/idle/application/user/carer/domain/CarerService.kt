package com.swm.idle.application.user.carer.domain

import com.swm.idle.application.common.converter.PointConverter
import com.swm.idle.domain.common.exception.PersistenceException
import com.swm.idle.domain.user.carer.entity.jpa.Carer
import com.swm.idle.domain.user.carer.enums.JobSearchStatus
import com.swm.idle.domain.user.carer.repository.jpa.CarerJpaRepository
import com.swm.idle.domain.user.carer.repository.jpa.CarerQueryRepository
import com.swm.idle.domain.user.common.enum.GenderType
import com.swm.idle.domain.user.common.vo.BirthYear
import com.swm.idle.domain.user.common.vo.PhoneNumber
import org.locationtech.jts.geom.Point
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Service
class CarerService(
    private val carerJpaRepository: CarerJpaRepository,
    private val carerQueryRepository: CarerQueryRepository,
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
                birthYear = birthYear.value,
                carerName = carerName,
                gender = gender,
                phoneNumber = phoneNumber.value,
                roadNameAddress = roadNameAddress,
                lotNumberAddress = lotNumberAddress,
                longitude = BigDecimal(longitude),
                latitude = BigDecimal(latitude),
                location = PointConverter.convertToPoint(
                    latitude = latitude.toDouble(),
                    longitude = longitude.toDouble(),
                )
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
        jobSearchStatus: JobSearchStatus?,
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
            location = PointConverter.convertToPoint(
                longitude.toDouble(),
                latitude.toDouble()
            )
        )
    }

    fun updateWithoutAddress(
        carer: Carer,
        experienceYear: Int?,
        introduce: String?,
        speciality: String?,
        jobSearchStatus: JobSearchStatus?,
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

    fun findAllByLocationWithinRadius(location: Point): List<Carer>? {
        return carerQueryRepository.findAllByLocationWithinRadius(location)
    }

}
