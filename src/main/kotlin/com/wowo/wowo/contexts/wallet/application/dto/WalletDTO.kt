package com.wowo.wowo.contexts.wallet.application.dto

import com.wowo.wowo.contexts.wallet.domain.entity.Wallet
import com.wowo.wowo.shared.domain.HasOwner
import com.wowo.wowo.shared.domain.OwnerType
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Data Transfer Object for Wallet.
 * 
 * The ownerName field is enriched separately using WalletOwnerEnricher.
 */
data class WalletDTO(
    val id: String,
    val balance: BigDecimal,
    val currency: String,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    override val ownerId: String,
    override val ownerType: OwnerType,
    override val ownerName: String?
) : HasOwner<String> {
    companion object {
        fun fromDomain(wallet: Wallet): WalletDTO {
            return WalletDTO(
                id = wallet.id.toString(),
                ownerId = wallet.ownerId,
                ownerType = wallet.ownerType,
                ownerName = null,  // Will be enriched later
                balance = wallet.getBalance().money.amount,
                currency = wallet.currency.name,
                isActive = wallet.isActive,
                createdAt = wallet.createdAt,
                updatedAt = wallet.updatedAt
            )
        }
    }
}


