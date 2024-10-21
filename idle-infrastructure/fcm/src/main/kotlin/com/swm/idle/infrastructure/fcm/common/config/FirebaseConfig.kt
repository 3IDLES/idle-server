package com.swm.idle.infrastructure.fcm.common.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.io.ByteArrayInputStream

@Configuration
class FirebaseConfig(
    @Value("\${firebase.json}")
    var firebaseJsonString: String,
) {

    private val logger = KotlinLogging.logger {}

    @PostConstruct
    fun initializeFirebaseApp() {
        try {
            val inputStream = ByteArrayInputStream(firebaseJsonString.toByteArray(Charsets.UTF_8))

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
