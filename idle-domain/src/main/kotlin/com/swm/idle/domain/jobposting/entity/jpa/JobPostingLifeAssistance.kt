package com.swm.idle.domain.jobposting.entity.jpa

import com.swm.idle.domain.common.entity.BaseEntity
import com.swm.idle.domain.jobposting.enums.LifeAssistanceType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "job_posting_life_assistance")
class JobPostingLifeAssistance(
    jobPostingId: UUID,
    lifeAssistance: LifeAssistanceType,
) : BaseEntity() {

    @Column(nullable = false)
    var jobPostingId: UUID = jobPostingId
        private set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var lifeAssistance: LifeAssistanceType = lifeAssistance
        private set

}
