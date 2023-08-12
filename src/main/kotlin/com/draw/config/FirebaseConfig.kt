package com.draw.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.io.ByteArrayInputStream
import java.util.Base64

@Profile("!test")
@Configuration
class FirebaseConfig(
    @Value("\${firebase.fcm.credential}") private val fcmCredential: String,
) {
    @Bean
    fun firebaseMessaging(): FirebaseMessaging {
        val decodedCredential = Base64.getDecoder().decode(fcmCredential)
        println(String(decodedCredential))
        val stream = ByteArrayInputStream(decodedCredential)

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(stream))
            .build()
        val firebaseMessaging = FirebaseApp.initializeApp(options)
        return FirebaseMessaging.getInstance(firebaseMessaging)
    }
}
