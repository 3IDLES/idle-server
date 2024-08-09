package com.swm.idle.domain.user.carer.entity.jpa

import com.swm.idle.domain.user.carer.enums.CarerAccountStatus
import com.swm.idle.domain.user.carer.enums.JobSearchStatus
import com.swm.idle.domain.user.common.entity.jpa.User
import com.swm.idle.domain.user.common.enum.GenderType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.hibernate.annotations.DynamicUpdate
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "carer")
@DynamicUpdate
class Carer(
    id: UUID,
    phoneNumber: String,
    carerName: String,
    birthYear: Int,
    gender: GenderType,
    roadNameAddress: String,
    lotNumberAddress: String,
    longitude: BigDecimal,
    latitude: BigDecimal,
    introduce: String? = null,
    experienceYear: Int? = null,
    speciality: String? = null,
    profileImageUrl: String? = null,
    jobSearchStatus: JobSearchStatus = JobSearchStatus.YES,
    carerAccountStatus: CarerAccountStatus = CarerAccountStatus.ACTIVE,
) : User(id, phoneNumber, carerName) {

    @Column(nullable = false)
    var birthYear: Int = birthYear
        private set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    var gender: GenderType = gender
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var roadNameAddress: String = roadNameAddress
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var lotNumberAddress: String = lotNumberAddress
        private set

    @Column(nullable = false, columnDefinition = "decimal(14, 10)")
    var longitude: BigDecimal = longitude
        private set

    @Column(nullable = false, columnDefinition = "decimal(14, 10)")
    var latitude: BigDecimal = latitude
        private set

    @Column(columnDefinition = "varchar(255)")
    var introduce: String? = introduce
        private set

    @Column(columnDefinition = "int")
    var experienceYear: Int? = experienceYear
        private set

    @Column(columnDefinition = "varchar(255)")
    var speciality: String? = speciality
        private set

    @Column(columnDefinition = "varchar(255)")
    var profileImageUrl: String? = profileImageUrl
        private set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20)")
    var jobSearchStatus: JobSearchStatus = jobSearchStatus
        private set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20)")
    var carerAccountStatus: CarerAccountStatus = carerAccountStatus
        private set

    fun update(
        experienceYear: Int?,
        roadNameAddress: String,
        lotNumberAddress: String,
        longitude: BigDecimal,
        latitude: BigDecimal,
        introduce: String?,
        speciality: String?,
        jobSearchStatus: JobSearchStatus,
    ) {
        this.experienceYear = experienceYear
        this.roadNameAddress = roadNameAddress
        this.lotNumberAddress = lotNumberAddress
        this.longitude = longitude
        this.latitude = latitude
        this.introduce = introduce
        this.speciality = speciality
        this.jobSearchStatus = jobSearchStatus
    }

    fun updateWithoutAddress(
        experienceYear: Int?,
        introduce: String?,
        speciality: String?,
        jobSearchStatus: JobSearchStatus,
    ) {
        this.experienceYear = experienceYear
        this.introduce = introduce
        this.speciality = speciality
        this.jobSearchStatus = jobSearchStatus
    }

}
