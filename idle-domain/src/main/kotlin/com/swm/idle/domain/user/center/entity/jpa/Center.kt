package com.swm.idle.domain.user.center.entity.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "center")
class Center(
    id: UUID,
    centerName: String,
    officeNumber: String,
    businessRegistrationNumber: String,
    roadNameAddress: String,
    lotNumberAddress: String,
    detailedAddress: String,
    longitude: BigDecimal,
    latitude: BigDecimal,
    profileImageUrl: String?,
    introduce: String?,
) {

    @Id
    @Column(nullable = false)
    var id: UUID = id
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var centerName: String = centerName
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var businessRegistrationNumber: String = businessRegistrationNumber
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var officeNumber: String = officeNumber
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var roadNameAddress: String = roadNameAddress
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var lotNumberAddress: String = lotNumberAddress
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var detailedAddress: String = detailedAddress
        private set

    @Column(nullable = false, columnDefinition = "decimal(14, 10)")
    var longitude: BigDecimal = longitude
        private set

    @Column(nullable = false, columnDefinition = "decimal(14, 10)")
    var latitude: BigDecimal = latitude
        private set

    @Column(columnDefinition = "varchar(255)")
    var profileImageUrl: String? = profileImageUrl
        private set

    @Column(columnDefinition = "varchar(255)")
    var introduce: String? = introduce
        private set


    fun updateProfileImageUrl(profileImageUrl: String) {
        this.profileImageUrl = profileImageUrl
    }

    fun update(officeNumber: String, introduce: String?) {
        this.officeNumber = officeNumber

        if (introduce != null) {
            this.introduce = introduce
        }
    }

}
