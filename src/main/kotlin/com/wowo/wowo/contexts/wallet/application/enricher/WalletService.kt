package com.wowo.wowo.contexts.wallet.application.enricher

import com.wowo.wowo.contexts.user.domain.repository.UserRepository
import com.wowo.wowo.contexts.wallet.domain.repository.WalletRepository
import org.springframework.stereotype.Service

@Service
class WalletService(val userRepository: UserRepository, val walletRepository: WalletRepository) {

}