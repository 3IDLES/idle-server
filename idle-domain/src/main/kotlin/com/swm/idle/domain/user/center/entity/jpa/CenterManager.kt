package com.swm.idle.domain.user.center.entity.jpa

import com.swm.idle.domain.user.center.enums.CenterManagerAccountStatus
import com.swm.idle.domain.user.common.entity.jpa.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(name = "center_manager")
class CenterManager(
    identifier: String,
    password: String,
    managerName: String,
    phoneNumber: String,
    status: CenterManagerAccountStatus,
    centerBusinessRegistrationNumber: String,
) : User(phoneNumber, managerName) {

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var identifier: String = identifier
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var password: String = password
        private set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    var status: CenterManagerAccountStatus = status
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var centerBusinessRegistrationNumber: String = centerBusinessRegistrationNumber
        private set

    fun updatePassword(newPassword: String) {
        this.password = newPassword
    }

    fun updateAccountStatusToPending() {
        this.status = CenterManagerAccountStatus.PENDING
    }

    fun isNew(): Boolean {
        return status == CenterManagerAccountStatus.NEW
    }

}
