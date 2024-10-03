package com.swm.idle.infrastructure.fcm.common.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
class FirebaseConfig {

    @Value("\${firebase.json.path}")
    lateinit var firebaseConfigJsonPath: String

    @PostConstruct
    fun initializeFirebaseApp() {
        val googleCredentials =
            GoogleCredentials.fromStream(ClassPathResource(firebaseConfigJsonPath).inputStream)

        val fireBaseOptions = FirebaseOptions.builder()
            .setCredentials(googleCredentials)
            .build()

        FirebaseApp.initializeApp(fireBaseOptions)
    }

}
