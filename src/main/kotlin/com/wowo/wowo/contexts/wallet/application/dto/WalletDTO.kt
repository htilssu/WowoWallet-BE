package com.wowo.wowo.contexts.wallet.application.dto

import com.wowo.wowo.contexts.wallet.domain.entity.*
import com.wowo.wowo.shared.domain.*
import java.math.*
import java.time.*

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
    val createdAt: Instant,
    val updatedAt: Instant,
    override var ownerId: String,
    override var ownerType: OwnerType,
    override var ownerName: String?
) : IHasOwner<String> {
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


