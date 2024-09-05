package com.swm.idle.application.user.carer.facade

import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.user.carer.domain.CarerService
import com.swm.idle.domain.user.carer.enums.JobSearchStatus
import com.swm.idle.domain.user.common.vo.BirthYear
import com.swm.idle.infrastructure.client.geocode.service.GeoCodeService
import com.swm.idle.support.transfer.user.carer.GetCarerProfileResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CarerFacadeService(
    private val carerService: CarerService,
    private val geoCodeService: GeoCodeService,
) {

    fun getMyCarerProfile(): GetCarerProfileResponse {
        val carerId = getUserAuthentication().userId

        return getCarerProfile(carerId)
    }

    fun getCarerProfile(carerId: UUID): GetCarerProfileResponse {
        carerService.getById(carerId).let {
            return GetCarerProfileResponse(
                carerId = it.id,
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
        roadNameAddress: String?,
        lotNumberAddress: String?,
        introduce: String?,
        speciality: String?,
        jobSearchStatus: JobSearchStatus?,
    ) {
        val carer = getUserAuthentication().userId.let {
            carerService.getById(it)
        }

        val shouldUpdateAddress = roadNameAddress != null && lotNumberAddress != null

        if (shouldUpdateAddress) {
            val geoCodeSearchResult = geoCodeService.search(roadNameAddress!!)

            carerService.update(
                carer = carer,
                experienceYear = experienceYear,
                roadNameAddress = roadNameAddress,
                lotNumberAddress = lotNumberAddress!!,
                longitude = geoCodeSearchResult.addresses[0].x,
                latitude = geoCodeSearchResult.addresses[0].y,
                introduce = introduce,
                speciality = speciality,
                jobSearchStatus = jobSearchStatus,
            )
        } else {
            carerService.updateWithoutAddress(
                carer = carer,
                experienceYear = experienceYear,
                introduce = introduce,
                speciality = speciality,
                jobSearchStatus = jobSearchStatus,
            )
        }
    }
}
