package com.swm.idle.infrastructure.fcm.common.client

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import org.springframework.stereotype.Component

@Component
class FcmClient {

    fun send(message: Message) {
        FirebaseMessaging.getInstance().sendAsync(message)
    }

}
