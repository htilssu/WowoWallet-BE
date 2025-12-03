package com.wowo.wowo.contexts.wallet.application.dto

import com.wowo.wowo.contexts.wallet.domain.entity.Wallet
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Data Transfer Object for Wallet
 */
data class WalletDTO(
    val id: String,
    val userId: String,
    val balance: BigDecimal,
    val currency: String,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromDomain(wallet: Wallet): WalletDTO {
            return WalletDTO(
                id = wallet.id.toString(),
                userId = wallet.userId,
                balance = wallet.getBalance().money.amount,
                currency = wallet.currency.name,
                isActive = wallet.isActive,
                createdAt = wallet.createdAt,
                updatedAt = wallet.updatedAt
            )
        }
    }
}

