package com.swm.idle.application.user.carer.facade

import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.user.carer.domain.CarerService
import com.swm.idle.domain.user.carer.enums.JobSearchStatus
import com.swm.idle.domain.user.common.vo.BirthYear
import com.swm.idle.support.transfer.user.carer.GetCarerProfileResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CarerFacadeService(
    private val carerService: CarerService,
) {

    fun getMyCarerProfile(): GetCarerProfileResponse {
        val carerId = getUserAuthentication().userId

        return getCarerProfile(carerId)
    }

    fun getCarerProfile(carerId: UUID): GetCarerProfileResponse {
        carerService.getById(carerId).let {
            return GetCarerProfileResponse(
                carerName = it.name,
                age = BirthYear.calculateAge(it.birthYear),
                gender = it.gender,
                experienceYear = it.experienceYear,
                phoneNumber = it.phoneNumber,
                roadNameAddress = it.roadNameAddress,
                lotNumberAddress = it.lotNumberAddress,
                longitude = it.longitude.toString(),
                latitude = it.latitude.toString(),
                introduce = it.introduce,
                speciality = it.speciality,
                profileImageUrl = it.profileImageUrl,
                jobSearchStatus = it.jobSearchStatus,
            )
        }
    }

    @Transactional
    fun updateCarerProfile(
        experienceYear: Int?,
        roadNameAddress: String,
        lotNumberAddress: String,
        longitude: String,
        latitude: String,
        introduce: String?,
        speciality: String?,
        jobSearchStatus: JobSearchStatus,
    ) {
        getUserAuthentication().userId.let {
            carerService.getById(it)
        }.also {
            carerService.update(
                carer = it,
                experienceYear = experienceYear,
                roadNameAddress = roadNameAddress,
                lotNumberAddress = lotNumberAddress,
                longitude = longitude,
                latitude = latitude,
                introduce = introduce,
                speciality = speciality,
                jobSearchStatus = it.jobSearchStatus,
            )
        }
    }

}
