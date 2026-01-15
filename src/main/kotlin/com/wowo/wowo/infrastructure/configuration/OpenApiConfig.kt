package com.wowo.wowo.infrastructure.configuration

import io.swagger.v3.oas.models.*
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.*


@Configuration
class OpenApiConfig {
    @Bean
    fun customOpenAPI(): OpenAPI? {
        return OpenAPI().info(Info().version("1.0").description("Wowo Application API"))
    }
}