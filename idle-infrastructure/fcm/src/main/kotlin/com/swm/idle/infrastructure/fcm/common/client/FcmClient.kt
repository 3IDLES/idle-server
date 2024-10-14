package com.swm.idle.infrastructure.fcm.common.client

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import org.springframework.stereotype.Component

@Component
class FcmClient {

    fun sendMulticast(message: MulticastMessage) {
        FirebaseMessaging.getInstance().sendEachForMulticast(message)
    }

}
