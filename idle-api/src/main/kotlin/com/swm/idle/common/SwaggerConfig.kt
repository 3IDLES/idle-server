package com.swm.idle.common

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig(
    @Value("{SERVER_URL:http://localhost:8080}")
    private val serverUrl: String,
) {

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .components(securityComponents())
            .servers(
                listOf(Server().apply { url = serverUrl})
            )
    }

    private fun securityComponents(): Components {
        return Components()
            .addSecuritySchemes(
                "AccessToken",
                SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
            )
    }

}
