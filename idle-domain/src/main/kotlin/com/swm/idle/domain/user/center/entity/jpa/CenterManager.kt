package com.swm.idle.domain.user.center.entity.jpa

import com.swm.idle.domain.user.center.enums.CenterAccountStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
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
) {

    @Id
    @Column(nullable = false)
    var id: UUID = id
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var identifier: String = identifier
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var password: String = password
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var managerName: String = managerName
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var phoneNumber: String = phoneNumber
        private set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255)")
    var status: CenterAccountStatus = status
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var centerBusinessRegistrationNumber: String = centerBusinessRegistrationNumber
        private set

}
