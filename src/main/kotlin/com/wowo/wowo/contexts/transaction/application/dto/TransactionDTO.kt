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
    val sourceWalletId: String?,
    var sourceOwnerId: String?,
    var sourceOwnerType: OwnerType?,
    val sourceOwnerName: String?,    // Enriched field
    
    // Receiver (to) wallet information  
    val targetWalletId: String?,
    var targetOwnerId: String?,
    var targetOwnerType: OwnerType?,
    val targetOwnerName: String?,      // Enriched field
    
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
            sourceWalletId: String?,
            targetWalletId: String?,
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
            sourceWalletId = sourceWalletId,
            sourceOwnerId = null,
            sourceOwnerType = null,
            sourceOwnerName = null,
            targetWalletId = targetWalletId,
            targetOwnerId = null,
            targetOwnerType = null,
            targetOwnerName = null,
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

