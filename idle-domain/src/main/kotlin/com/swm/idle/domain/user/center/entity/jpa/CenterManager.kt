package com.swm.idle.domain.user.center.entity.jpa

import com.swm.idle.domain.user.center.enums.CenterAccountStatus
import com.swm.idle.domain.user.common.entity.jpa.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "center_manager")
class CenterManager(
    id: UUID,
    identifier: String,
    password: String,
    managerName: String,
    phoneNumber: String,
    status: CenterAccountStatus,
    centerBusinessRegistrationNumber: String,
) : User(id, phoneNumber, managerName) {

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var identifier: String = identifier
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var password: String = password
        private set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    var status: CenterAccountStatus = status
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var centerBusinessRegistrationNumber: String = centerBusinessRegistrationNumber
        private set

}
