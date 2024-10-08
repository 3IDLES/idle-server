package com.swm.idle.domain.applys.entity.jpa

import com.swm.idle.domain.common.entity.BaseEntity
import com.swm.idle.domain.jobposting.enums.ApplyMethodType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "applys")
class Applys(
    jobPostingId: UUID,
    carerId: UUID,
    applyMethodType: ApplyMethodType,
) : BaseEntity() {

    @Column(nullable = false)
    var jobPostingId: UUID = jobPostingId
        private set

    @Column(nullable = false)
    var carerId: UUID = carerId
        private set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var applyMethodType: ApplyMethodType = applyMethodType
        private set

}
