package com.swm.idle.application.jobposting.event

import com.swm.idle.domain.jobposting.event.CreateJobPostingEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class CreateJobPostingEventPublisher(
    private val eventPublisher: ApplicationEventPublisher,
) {

    fun publish(createJobPostingEvent: CreateJobPostingEvent) {
        eventPublisher.publishEvent(createJobPostingEvent)
    }

}
