package com.swm.idle.domain.user.common.entity.jpa

import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.util.*

@MappedSuperclass
open class User(
    id: UUID,
    phoneNumber: String,
    name: String,
) {

    @Id
    @Column(nullable = false)
    var id: UUID = id
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var phoneNumber: String = phoneNumber
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var name: String = name
        private set

}
