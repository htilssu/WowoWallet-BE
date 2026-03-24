package com.wowo.wowo.contexts.transaction.application.dto

import com.wowo.wowo.shared.domain.OwnerType
import java.math.BigDecimal
import java.time.Instant

/**
 * Data Transfer Object for Transaction.
 * 
 * Supports enrichment for both sender and receiver owner names,
 * with support for different owner types (USER, GROUP, FUND_GROUP).
 */
data class TransactionDTO(
    val id: String,
    
    // Sender (from) wallet information
    val fromWalletId: String?,
    val fromOwnerId: String?,
    val fromOwnerType: OwnerType?,
    val fromOwnerName: String?,    // Enriched field
    
    // Receiver (to) wallet information  
    val toWalletId: String?,
    val toOwnerId: String?,
    val toOwnerType: OwnerType?,
    val toOwnerName: String?,      // Enriched field
    
    // Transaction details
    val amount: BigDecimal,
    val currency: String,
    val type: String,
    val status: String,
    val description: String?,
    val reference: String?,
    val createdAt: Instant,
    val updatedAt: Instant,
    val normalizedDescription: String? = null,
    val transactionCategory: String? = null,
    val merchantName: String? = null,
    val merchantCategory: String? = null,
    val geoCountry: String? = null,
    val geoCity: String? = null,
    val riskScore: Int? = null,
    val riskLevel: String? = null,
    val tags: List<String> = emptyList()
) {
    companion object {
        /**
         * Create a DTO without enrichment (for initial mapping).
         */
        fun unenriched(
            id: String,
            fromWalletId: String?,
            toWalletId: String?,
            amount: BigDecimal,
            currency: String,
            type: String,
            status: String,
            description: String?,
            reference: String?,
            createdAt: Instant,
            updatedAt: Instant
        ) = TransactionDTO(
            id = id,
            fromWalletId = fromWalletId,
            fromOwnerId = null,
            fromOwnerType = null,
            fromOwnerName = null,
            toWalletId = toWalletId,
            toOwnerId = null,
            toOwnerType = null,
            toOwnerName = null,
            amount = amount,
            currency = currency,
            type = type,
            status = status,
            description = description,
            reference = reference,
            createdAt = createdAt,
            updatedAt = updatedAt,
            normalizedDescription = null,
            transactionCategory = null,
            merchantName = null,
            merchantCategory = null,
            geoCountry = null,
            geoCity = null,
            riskScore = null,
            riskLevel = null,
            tags = emptyList()
        )
    }
}

