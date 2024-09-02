package com.swm.idle.domain.jobposting.entity.jpa

import com.swm.idle.domain.common.entity.BaseEntity
import com.swm.idle.domain.jobposting.enums.JobPostingType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "job_posting_favorite")
class JobPostingFavorite(
    carerId: UUID,
    jobPostingId: UUID,
    jobPostingType: JobPostingType,
) : BaseEntity() {

    @Column(nullable = false)
    var carerId: UUID = carerId
        private set

    @Column(nullable = false)
    var jobPostingId: UUID = jobPostingId
        private set

    @Column(nullable = false)
    var jobPostingType: JobPostingType = jobPostingType
        private set

}
