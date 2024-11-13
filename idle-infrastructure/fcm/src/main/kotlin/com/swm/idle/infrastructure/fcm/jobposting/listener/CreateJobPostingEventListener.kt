package com.swm.idle.infrastructure.fcm.jobposting.listener

import com.swm.idle.domain.jobposting.event.CreateJobPostingEvent
import com.swm.idle.infrastructure.fcm.jobposting.service.CreateJobPostingEventService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class CreateJobPostingEventListener(
    private val createJobPostingEventService: CreateJobPostingEventService,
) {

    @EventListener
    fun handleCreateJobPostingEvent(createJobPostingEvent: CreateJobPostingEvent) {
        createJobPostingEventService.send(createJobPostingEvent)
    }
}
