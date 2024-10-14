package com.swm.idle.infrastructure.fcm.applys.service

import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import com.swm.idle.domain.applys.event.ApplyEvent
import com.swm.idle.domain.user.common.vo.BirthYear
import com.swm.idle.infrastructure.fcm.common.client.FcmClient
import com.swm.idle.infrastructure.fcm.common.enums.DestinationType
import org.springframework.stereotype.Component

@Component
class CarerApplyEventService(
    private val fcmClient: FcmClient,
) {


    fun sendForMulticast(applyEvent: ApplyEvent) {
        createMulticastMessage(applyEvent).also {
            fcmClient.sendMulticast(it)
        }
    }

    private fun createApplyNotification(applyEvent: ApplyEvent): Notification {
        return Notification.builder()
            .setTitle("${applyEvent.carer.name} 님이 공고에 지원하였습니다.")
            .setBody(createBodyMessage(applyEvent))
            .build()
    }

    private fun createBodyMessage(applyEvent: ApplyEvent): String {
        val filteredLotNumberAddress = applyEvent.jobPosting.lotNumberAddress.split(" ")
            .take(3)
            .joinToString(" ")

        return "$filteredLotNumberAddress " +
                "${applyEvent.jobPosting.careLevel}등급 " +
                "${BirthYear.calculateAge(applyEvent.jobPosting.birthYear)}세 " +
                applyEvent.jobPosting.gender.value
    }

    private fun createMulticastMessage(applyEvent: ApplyEvent): MulticastMessage {
        val applyNotification = createApplyNotification(applyEvent)

        val deviceTokens = applyEvent.deviceTokens.map { deviceToken ->
            deviceToken.deviceToken
        }

        return MulticastMessage.builder()
            .addAllTokens(deviceTokens)
            .setNotification(applyNotification)
            .putData("destination", DestinationType.APPLICANTS.toString())
            .putData("jobPostingId", applyEvent.jobPosting.id.toString())
            .build();
    }

}
