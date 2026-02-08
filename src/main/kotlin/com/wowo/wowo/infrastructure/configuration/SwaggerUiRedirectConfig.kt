package com.wowo.wowo.infrastructure.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class SwaggerUiRedirectConfig : WebMvcConfigurer {
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addRedirectViewController("/swagger-ui", "/swagger-ui/index.html")
        registry.addRedirectViewController("/swagger-ui/", "/swagger-ui/index.html")
    }
}
