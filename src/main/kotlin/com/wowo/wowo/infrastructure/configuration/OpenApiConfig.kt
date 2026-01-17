package com.wowo.wowo.infrastructure.configuration

import io.swagger.v3.oas.models.*
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.*


@Configuration
class OpenApiConfig {
    @Bean
    fun customOpenAPI(): OpenAPI? {
        return OpenAPI().addSecurityItem(
            SecurityRequirement().addList("bearerAuth")
        ).components(
            Components().addSecuritySchemes(
                "bearerAuth", SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
            )
        ).info(Info().version("1.0").description("Wowo Application API"))
    }
}