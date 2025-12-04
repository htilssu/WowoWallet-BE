package com.wowo.wowo.infrastructure.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

/**
 * Spring Security Configuration
 * - Public endpoints: /users/register, /health
 * - Protected endpoints: all others (require authentication)
 * - Method-level security enabled with @PreAuthorize
 * - Custom authorization annotations: @RequireRole, @RequirePermission, @RequireAdmin, etc.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EnableAspectJAutoProxy
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } // Disable CSRF for REST API
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/users/register").permitAll()
                    .requestMatchers("/actuator/*").permitAll()// Public registration endpoint
                    .anyRequest().authenticated() // All other endpoints require authentication
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session for REST API
            }
            .httpBasic { } // Enable HTTP Basic authentication (can be replaced with JWT later)

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}

