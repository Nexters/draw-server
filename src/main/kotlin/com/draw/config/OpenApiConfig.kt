package com.draw.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class OpenApiConfig {

    @Bean
    fun openAPI(env: Environment): OpenAPI {

        val swaggerUrl =
            if (env.activeProfiles.contains("prod")) {
                "https://draw-nexters.kro.kr"
            } else {
                "http://localhost:8080"
            }

        return OpenAPI()
            .components(
                Components()
//                    .addSecuritySchemes(
//                        "Authorization",
//                        SecurityScheme()
//                            .type(SecurityScheme.Type.HTTP)
//                            .scheme("bearer").bearerFormat("JWT")
//                    )
            ).addServersItem(
                Server().url(swaggerUrl)
            ).info(
//            .addSecurityItem(
//                SecurityRequirement().addList("Authorization")
//            )
                Info()
                    .title("Draw API")
                    .description("Draw API")
                    .version("v1")
            )
    }
}
