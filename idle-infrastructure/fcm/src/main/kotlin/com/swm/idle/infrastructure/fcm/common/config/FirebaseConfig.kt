package com.swm.idle.infrastructure.fcm.common.config

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class FirebaseConfig(
    @Value("\${firebase.json.path}")
    var firebaseConfigJsonPath: String,
) {

    private val logger = KotlinLogging.logger {}

//    @PostConstruct
//    fun initializeFirebaseApp() {
//        val googleCredentials =
//            GoogleCredentials.fromStream(ClassPathResource(firebaseConfigJsonPath).inputStream)
//
//        val fireBaseOptions = FirebaseOptions.builder()
//            .setCredentials(googleCredentials)
//            .build()
//
//        FirebaseApp.initializeApp(fireBaseOptions)
//    }

    @PostConstruct
    fun printWorkingDirectory() {
        logger.warn { "Current working directory: " + System.getProperty("user.dir") }
    }


}
