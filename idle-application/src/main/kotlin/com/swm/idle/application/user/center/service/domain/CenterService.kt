package com.swm.idle.application.user.center.service.domain

import com.swm.idle.domain.common.exception.PersistenceException
import com.swm.idle.domain.user.center.entity.jpa.Center
import com.swm.idle.domain.user.center.repository.jpa.CenterJpaRepository
import com.swm.idle.domain.user.center.vo.BusinessRegistrationNumber
import com.swm.idle.domain.user.common.vo.OfficeNumber
import com.swm.idle.support.common.uuid.UuidCreator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Service
class CenterService(
    private val centerJpaRepository: CenterJpaRepository,
) {

    fun findByBusinessRegistrationNumber(businessRegistrationNumber: BusinessRegistrationNumber): Center? {
        return centerJpaRepository.findByBusinessRegistrationNumber(businessRegistrationNumber.value)
    }

    @Transactional
    fun create(
        officeNumber: OfficeNumber,
        centerName: String,
        businessRegistrationNumber: BusinessRegistrationNumber,
        roadNameAddress: String,
        lotNumberAddress: String,
        detailedAddress: String,
        longitude: String,
        latitude: String,
        introduce: String?,
    ) {
        centerJpaRepository.save(
            Center(
                id = UuidCreator.create(),
                officeNumber = officeNumber.value,
                centerName = centerName,
                businessRegistrationNumber = businessRegistrationNumber.value,
                roadNameAddress = roadNameAddress,
                lotNumberAddress = lotNumberAddress,
                detailedAddress = detailedAddress,
                longitude = BigDecimal(longitude),
                latitude = BigDecimal(latitude),
                introduce = introduce,
                profileImageUrl = null,
            )
        )
    }

    fun update(
        center: Center,
        officeNumber: OfficeNumber?,
        introduce: String?,
    ) {
        center.update(
            officeNumber = officeNumber?.value,
            introduce = introduce,
        )
    }

    /**
     * Retrieves a Center entity by its unique identifier.
     *
     * @param centerId The UUID of the Center to retrieve.
     * @return The Center entity with the specified ID.
     * @throws PersistenceException.ResourceNotFound if no Center with the given ID exists.
     */
    fun getById(centerId: UUID): Center {
        return centerJpaRepository.findByIdOrNull(centerId)
            ?: throw PersistenceException.ResourceNotFound("Center(id=$centerId)를 찾을 수 없습니다")
    }

    /**
     * Retrieves a list of Center entities matching the provided set of center IDs.
     *
     * @param centerIds Set of UUIDs representing the IDs of the centers to retrieve.
     * @return List of Center entities corresponding to the given IDs. If no centers are found for some IDs, those are omitted from the result.
     */
    fun getByIds(centerIds : Set<UUID>) : List<Center> {
        return centerJpaRepository.findAllById(centerIds)
    }

}
