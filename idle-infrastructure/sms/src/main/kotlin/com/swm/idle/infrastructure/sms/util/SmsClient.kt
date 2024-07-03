package com.swm.idle.infrastructure.sms.util

import com.swm.idle.infrastructure.sms.common.properties.SmsProperties
import net.nurigo.sdk.NurigoApp
import net.nurigo.sdk.message.model.Message
import net.nurigo.sdk.message.request.SingleMessageSendingRequest
import net.nurigo.sdk.message.service.DefaultMessageService
import org.springframework.stereotype.Component

@Component
class SmsClient(
    val smsProperties: SmsProperties,
) {
    val messageService: DefaultMessageService by lazy {
        NurigoApp.initialize(
            apiKey = smsProperties.accessKey,
            apiSecretKey = smsProperties.secretKey,
            domain = "https://api.coolsms.co.kr"
        )
    }

    fun sendMessage(to: String, content: String) {
        val message = Message(
            from = smsProperties.sendingNumber,
            to = to,
            text = content,
        )

        messageService.sendOne(SingleMessageSendingRequest(message))
    }
}
