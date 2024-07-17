package com.swm.idle.domain.user.common.entity.jpa

import com.swm.idle.domain.user.common.enum.UserType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "deleted_user_info")
class DeletedUserInfo(
    id: UUID,
    phoneNumber: String,
    role: UserType,
    reason: String,
    deletedAt: LocalDateTime,
) {

    @Id
    @Column(nullable = false)
    var id: UUID = id
        private set

    @Column(nullable = false)
    var phoneNumber: String = phoneNumber
        private set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: UserType = role
        private set

    @Column(nullable = false)
    var reason: String = reason
        private set

    @Column(nullable = false)
    var deletedAt: LocalDateTime = deletedAt
        private set

}
