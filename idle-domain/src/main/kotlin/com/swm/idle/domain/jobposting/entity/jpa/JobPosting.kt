package com.swm.idle.domain.jobposting.entity.jpa

import com.swm.idle.domain.jobposting.vo.ApplyDeadlineType
import com.swm.idle.domain.jobposting.vo.MentalStatus
import com.swm.idle.domain.jobposting.vo.PayType
import com.swm.idle.domain.user.common.enum.GenderType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "job_posting")
class JobPosting(
    id: UUID,
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
    speciality: String?,
    isExperiencePreferred: Boolean,
    applyDeadline: LocalDate,
    applyDeadlineType: ApplyDeadlineType,
) {

    @Id
    @Column(nullable = false)
    var id: UUID = id
        private set

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
    var speciality: String? = speciality
        private set

    @Column(nullable = false)
    var isExperiencePreferred: Boolean = isExperiencePreferred
        private set

    @Column(nullable = false)
    var applyDeadline: LocalDate = applyDeadline
        private set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var applyDeadlineType: ApplyDeadlineType = applyDeadlineType
        private set

}
