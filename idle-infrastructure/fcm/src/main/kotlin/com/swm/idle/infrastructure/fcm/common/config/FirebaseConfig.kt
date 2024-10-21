package com.swm.idle.infrastructure.fcm.common.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.io.ByteArrayInputStream
import java.util.*

@Configuration
class FirebaseConfig(
    @Value("\${firebase.json}")
    private val firebaseJsonBase64: String,
) {

    private val logger = KotlinLogging.logger {}

    @PostConstruct
    fun initializeFirebaseApp() {
        try {
            val decodedBytes = Base64.getDecoder().decode(firebaseJsonBase64)
            val inputStream = ByteArrayInputStream(decodedBytes)

            val googleCredentials = GoogleCredentials.fromStream(inputStream)

            val fireBaseOptions = FirebaseOptions.builder()
                .setCredentials(googleCredentials)
                .build()

            FirebaseApp.initializeApp(fireBaseOptions)

            logger.info { "FirebaseApp has been initialized successfully." }
        } catch (e: Exception) {
            logger.error(e) { "Error initializing FirebaseApp." }
        }
    }

}
