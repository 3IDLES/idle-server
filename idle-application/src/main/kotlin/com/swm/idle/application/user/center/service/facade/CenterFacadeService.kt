package com.swm.idle.application.user.center.service.facade

import com.swm.idle.application.common.security.getUserAuthentication
import com.swm.idle.application.user.center.service.domain.CenterManagerService
import com.swm.idle.application.user.center.service.domain.CenterService
import com.swm.idle.domain.user.center.exception.CenterException
import com.swm.idle.domain.user.center.vo.BusinessRegistrationNumber
import com.swm.idle.infrastructure.client.geocode.service.GeoCodeService
import com.swm.idle.support.transfer.user.center.GetCenterProfileResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CenterFacadeService(
    private val centerService: CenterService,
    private val centerManagerService: CenterManagerService,
    private val geoCodeService: GeoCodeService,
) {

    fun create(
        officeNumber: String,
        centerName: String,
        roadNameAddress: String,
        lotNumberAddress: String,
        detailedAddress: String,
        introduce: String?,
    ) {
        val centerManager = getUserAuthentication().userId.let {
            centerManagerService.getById(it)
        }

        centerService.findByBusinessRegistrationNumber(BusinessRegistrationNumber(centerManager.centerBusinessRegistrationNumber))
            ?.let {
                throw CenterException.AlreadyExistCenter()
            } ?: also {

            val result = geoCodeService.search(roadNameAddress)

            centerService.create(
                officeNumber = officeNumber,
                centerName = centerName,
                businessRegistrationNumber = BusinessRegistrationNumber(centerManager.centerBusinessRegistrationNumber),
                roadNameAddress = roadNameAddress,
                lotNumberAddress = lotNumberAddress,
                detailedAddress = detailedAddress,
                latitude = result.addresses[0].y,
                longitude = result.addresses[0].x,
                introduce = introduce,
            )
        }
    }

    @Transactional
    fun update(officeNumber: String?, introduce: String?) {
        val centerManager = getUserAuthentication().userId.let {
            centerManagerService.getById(it)
        }

        centerService.findByBusinessRegistrationNumber(BusinessRegistrationNumber(centerManager.centerBusinessRegistrationNumber))
            ?.let {
                centerService.update(
                    center = it,
                    officeNumber = officeNumber,
                    introduce = introduce,
                )
            } ?: throw CenterException.NotFoundException()
    }

    fun getMyProfile(): GetCenterProfileResponse {
        val centerManager = getUserAuthentication().userId.let {
            centerManagerService.getById(it)
        }

        val center =
            centerService.findByBusinessRegistrationNumber(BusinessRegistrationNumber(centerManager.centerBusinessRegistrationNumber))
                ?: throw CenterException.NotFoundException()

        return GetCenterProfileResponse(
            centerName = center.centerName,
            officeNumber = center.officeNumber,
            roadNameAddress = center.roadNameAddress,
            lotNumberAddress = center.lotNumberAddress,
            detailedAddress = center.detailedAddress,
            longitude = center.longitude.toString(),
            latitude = center.latitude.toString(),
            introduce = center.introduce,
            profileImageUrl = center.profileImageUrl,
        )
    }

    fun getCenterProfile(centerId: UUID): GetCenterProfileResponse {
        val center = centerService.getById(centerId)

        return GetCenterProfileResponse(
            centerName = center.centerName,
            officeNumber = center.officeNumber,
            roadNameAddress = center.roadNameAddress,
            lotNumberAddress = center.lotNumberAddress,
            detailedAddress = center.detailedAddress,
            longitude = center.longitude.toString(),
            latitude = center.latitude.toString(),
            introduce = center.introduce,
            profileImageUrl = center.profileImageUrl,
        )
    }

}
