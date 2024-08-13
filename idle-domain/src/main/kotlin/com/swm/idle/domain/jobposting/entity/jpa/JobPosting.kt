package com.swm.idle.domain.jobposting.entity.jpa

import com.swm.idle.domain.common.entity.BaseEntity
import com.swm.idle.domain.jobposting.vo.ApplyDeadlineType
import com.swm.idle.domain.jobposting.vo.JobPostingStatus
import com.swm.idle.domain.jobposting.vo.MentalStatus
import com.swm.idle.domain.jobposting.vo.PayType
import com.swm.idle.domain.user.common.enum.GenderType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.locationtech.jts.geom.Point
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "job_posting")
class JobPosting(
    centerId: UUID,
    startTime: String,
    endTime: String,
    payType: PayType,
    payAmount: Int,
    roadNameAddress: String,
    lotNumberAddress: String,
    longitude: BigDecimal?,
    latitude: BigDecimal?,
    clientName: String,
    gender: GenderType,
    birthYear: Int,
    weight: Int?,
    careLevel: Int,
    mentalStatus: MentalStatus,
    disease: String?,
    isMealAssistance: Boolean,
    isBowelAssistance: Boolean,
    isWalkingAssistance: Boolean,
    extraRequirement: String?,
    isExperiencePreferred: Boolean,
    applyDeadline: LocalDate?,
    applyDeadlineType: ApplyDeadlineType,
    location: Point,
) : BaseEntity() {

    @Column(nullable = false)
    var centerId: UUID = centerId
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var startTime: String = startTime
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var endTime: String = endTime
        private set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var payType: PayType = payType
        private set

    @Column(nullable = false, columnDefinition = "int")
    var payAmount: Int = payAmount
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var roadNameAddress: String = roadNameAddress
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var lotNumberAddress: String = lotNumberAddress
        private set

    @Column(columnDefinition = "decimal(14, 10)")
    var longitude: BigDecimal? = longitude
        private set

    @Column(columnDefinition = "decimal(14, 10)")
    var latitude: BigDecimal? = latitude
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var clientName: String = clientName
        private set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var gender: GenderType = gender
        private set

    @Column(nullable = false, columnDefinition = "int")
    var birthYear: Int = birthYear
        private set

    @Column(columnDefinition = "int")
    var weight: Int? = weight
        private set

    @Column(nullable = false)
    var careLevel: Int = careLevel
        private set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var mentalStatus: MentalStatus = mentalStatus
        private set

    @Column(columnDefinition = "TEXT")
    var disease: String? = disease
        private set

    @Column(nullable = false)
    var isMealAssistance: Boolean = isMealAssistance
        private set

    @Column(nullable = false)
    var isBowelAssistance: Boolean = isBowelAssistance
        private set

    @Column(nullable = false)
    var isWalkingAssistance: Boolean = isWalkingAssistance
        private set

    @Column(columnDefinition = "TEXT")
    var extraRequirement: String? = extraRequirement
        private set

    @Column(nullable = false)
    var isExperiencePreferred: Boolean = isExperiencePreferred
        private set

    @Column(nullable = true)
    var applyDeadline: LocalDate? = applyDeadline
        private set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var applyDeadlineType: ApplyDeadlineType = applyDeadlineType
        private set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var jobPostingStatus: JobPostingStatus = JobPostingStatus.IN_PROGRESS
        private set

    @Column(nullable = false, columnDefinition = "POINT SRID 4326")
    var location: Point = location
        private set

    fun update(
        startTime: String?,
        endTime: String?,
        payType: PayType?,
        payAmount: Int?,
        roadNameAddress: String,
        lotNumberAddress: String,
        longitude: BigDecimal,
        latitude: BigDecimal,
        clientName: String?,
        gender: GenderType?,
        birthYear: Int?,
        weight: Int?,
        careLevel: Int?,
        mentalStatus: MentalStatus?,
        disease: String?,
        isMealAssistance: Boolean?,
        isBowelAssistance: Boolean?,
        isWalkingAssistance: Boolean?,
        extraRequirement: String?,
        isExperiencePreferred: Boolean?,
        applyDeadline: LocalDate?,
        applyDeadlineType: ApplyDeadlineType?,
    ) {
        this.startTime = startTime ?: this.startTime
        this.endTime = endTime ?: this.endTime
        this.payType = payType ?: this.payType
        this.payAmount = payAmount ?: this.payAmount
        this.roadNameAddress = roadNameAddress
        this.lotNumberAddress = lotNumberAddress
        this.longitude = longitude
        this.latitude = latitude
        this.clientName = clientName ?: this.clientName
        this.gender = gender ?: this.gender
        this.birthYear = birthYear ?: this.birthYear
        this.weight = weight
        this.careLevel = careLevel ?: this.careLevel
        this.mentalStatus = mentalStatus ?: this.mentalStatus
        this.disease = disease
        this.isMealAssistance = isMealAssistance ?: this.isMealAssistance
        this.isBowelAssistance = isBowelAssistance ?: this.isBowelAssistance
        this.isWalkingAssistance = isWalkingAssistance ?: this.isWalkingAssistance
        this.extraRequirement = extraRequirement
        this.isExperiencePreferred = isExperiencePreferred ?: this.isExperiencePreferred
        this.applyDeadline = applyDeadline ?: this.applyDeadline
        this.applyDeadlineType = applyDeadlineType ?: this.applyDeadlineType
    }

    fun updateWithoutAddress(
        startTime: String?,
        endTime: String?,
        payType: PayType?,
        payAmount: Int?,
        clientName: String?,
        gender: GenderType?,
        birthYear: Int?,
        weight: Int?,
        careLevel: Int?,
        mentalStatus: MentalStatus?,
        disease: String?,
        isMealAssistance: Boolean?,
        isBowelAssistance: Boolean?,
        isWalkingAssistance: Boolean?,
        extraRequirement: String?,
        isExperiencePreferred: Boolean?,
        applyDeadline: LocalDate?,
        applyDeadlineType: ApplyDeadlineType?,
    ) {
        this.startTime = startTime ?: this.startTime
        this.endTime = endTime ?: this.endTime
        this.payType = payType ?: this.payType
        this.payAmount = payAmount ?: this.payAmount
        this.clientName = clientName ?: this.clientName
        this.gender = gender ?: this.gender
        this.birthYear = birthYear ?: this.birthYear
        this.weight = weight
        this.careLevel = careLevel ?: this.careLevel
        this.mentalStatus = mentalStatus ?: this.mentalStatus
        this.disease = disease
        this.isMealAssistance = isMealAssistance ?: this.isMealAssistance
        this.isBowelAssistance = isBowelAssistance ?: this.isBowelAssistance
        this.isWalkingAssistance = isWalkingAssistance ?: this.isWalkingAssistance
        this.extraRequirement = extraRequirement
        this.isExperiencePreferred = isExperiencePreferred ?: this.isExperiencePreferred
        this.applyDeadline = applyDeadline ?: this.applyDeadline
        this.applyDeadlineType = applyDeadlineType ?: this.applyDeadlineType
    }

    fun updateToComplete() {
        this.jobPostingStatus = JobPostingStatus.COMPLETED
    }

}
