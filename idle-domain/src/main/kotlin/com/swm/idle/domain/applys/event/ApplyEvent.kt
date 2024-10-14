package com.swm.idle.domain.applys.event

import com.swm.idle.domain.jobposting.entity.jpa.JobPosting
import com.swm.idle.domain.notification.jpa.DeviceToken
import com.swm.idle.domain.user.carer.entity.jpa.Carer

data class ApplyEvent(
    val deviceTokens: List<DeviceToken>,
    val jobPosting: JobPosting,
    val carer: Carer,
) {

    companion object {

        fun createApplyEvent(
            deviceTokens: List<DeviceToken>,
            jobPosting: JobPosting,
            carer: Carer,
        ): ApplyEvent {
            return ApplyEvent(deviceTokens, jobPosting, carer)
        }

    }

}
