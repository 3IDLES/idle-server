package com.swm.idle.domain.jobposting.entity.jpa

import com.swm.idle.domain.jobposting.vo.ApplyMethodType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "job_posting_apply_method")
class JobPostingApplyMethod(
    id: UUID,
    jobPostingId: UUID,
    applyMethod: ApplyMethodType,
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
    var applyMethod: ApplyMethodType = applyMethod
        private set

}
