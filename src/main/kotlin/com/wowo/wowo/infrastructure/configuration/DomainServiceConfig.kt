package com.wowo.wowo.infrastructure.configuration

import com.wowo.wowo.contexts.transaction.domain.acl.WalletACL
import com.wowo.wowo.contexts.transaction.domain.service.TransferDomainService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration for Domain Services
 * Domain services are configured here to maintain their independence from infrastructure
 */
@Configuration
class DomainServiceConfig {

    @Bean
    fun transferDomainService(walletACL: WalletACL): TransferDomainService {
        return TransferDomainService(walletACL)
    }
}

