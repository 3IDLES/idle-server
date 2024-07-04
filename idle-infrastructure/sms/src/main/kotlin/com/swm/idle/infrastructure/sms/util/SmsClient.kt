package com.swm.idle.infrastructure.sms.util

import com.swm.idle.infrastructure.sms.common.properties.SmsApiProperties
import net.nurigo.sdk.NurigoApp
import net.nurigo.sdk.message.model.Message
import net.nurigo.sdk.message.request.SingleMessageSendingRequest
import net.nurigo.sdk.message.service.DefaultMessageService
import org.springframework.stereotype.Component

@Component
class SmsClient(
    val smsApiProperties: SmsApiProperties,
) {
    val messageService: DefaultMessageService by lazy {
        NurigoApp.initialize(
            apiKey = smsApiProperties.accessKey,
            apiSecretKey = smsApiProperties.secretKey,
            domain = "https://api.coolsms.co.kr"
        )
    }

    fun sendMessage(to: String, content: String) {
        val message = Message(
            from = smsApiProperties.sendingNumber,
            to = to,
            text = content,
        )

        messageService.sendOne(SingleMessageSendingRequest(message))
    }
}
