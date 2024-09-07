package com.swm.idle.domain.jobposting.entity.jpa

import com.swm.idle.domain.common.entity.BaseEntity
import com.swm.idle.domain.jobposting.enums.Weekdays
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "job_posting_weekday")
class JobPostingWeekday(
    jobPostingId: UUID,
    weekday: Weekdays,
) : BaseEntity() {

    @Column(nullable = false)
    var jobPostingId: UUID = jobPostingId
        private set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var weekday: Weekdays = weekday
        private set

}
