package com.swm.idle.domain.user.common.entity.jpa

import com.swm.idle.domain.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
open class User(
    phoneNumber: String,
    name: String,
) : BaseEntity() {

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var phoneNumber: String = phoneNumber
        private set

    @Column(nullable = false, columnDefinition = "varchar(255)")
    var name: String = name
        private set

}
