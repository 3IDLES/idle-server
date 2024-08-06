package com.swm.idle.domain.jobposting.entity.jpa

import com.swm.idle.domain.jobposting.vo.LifeAssistanceType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "job_posting_life_assistance")
class JobPostingLifeAssistance(
    id: UUID,
    jobPostingId: UUID,
    lifeAssistance: LifeAssistanceType,
) {

    @Id
    @Column(nullable = false)
    var id: UUID = id
        private set

    @Column(nullable = false)
    var jobPostingId: UUID = jobPostingId
        private set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var lifeAssistance: LifeAssistanceType = lifeAssistance
        private set

}
