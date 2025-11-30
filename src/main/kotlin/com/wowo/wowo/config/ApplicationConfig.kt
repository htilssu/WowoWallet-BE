package com.wowo.wowo.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

/**
 * Spring Configuration for DDD contexts
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = [
        "com.wowo.wowo.contexts.user.infrastructure.persistence",
        "com.wowo.wowo.contexts.wallet.infrastructure.persistence",
        "com.wowo.wowo.contexts.transaction.infrastructure.persistence"
    ]
)
@ComponentScan(basePackages = ["com.wowo.wowo"])
class ApplicationConfig

