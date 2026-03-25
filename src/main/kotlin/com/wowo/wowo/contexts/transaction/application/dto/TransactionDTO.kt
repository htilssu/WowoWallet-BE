package com.wowo.wowo.contexts.transaction.application.dto

import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionStatus
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionType
import com.wowo.wowo.shared.domain.OwnerType
import java.math.BigDecimal
import java.time.Instant

data class TransactionDTO(
    // -- 1. Identity --
    val id: String,
    val reference: String?,
    val type: TransactionType,
    val status: TransactionStatus,

    // -- 2. Parties --
    val sender: PartyDTO,
    val receiver: PartyDTO,

    // -- 3. Money --
    val amount: MoneyDTO,

    // -- 4. Lifecycle --
    val createdAt: Instant,
    val updatedAt: Instant,
    val completedAt: Instant? = null,

    // -- 5. Enrichment (optional) --
    val description: String?,
    val category: String? = null,
    val merchant: MerchantDTO? = null,
    val location: LocationDTO? = null,
    val risk: RiskDTO? = null,
    val tags: List<String> = emptyList(),
    val metadata: Map<String, String> = emptyMap()
) {
    val sourceWalletId: String?
        get() = sender.walletId

    val targetWalletId: String?
        get() = receiver.walletId

    val currency: String
        get() = amount.currency

    var sourceOwnerId: String?
        get() = sender.ownerId
        set(value) {
            sender.ownerId = value
        }

    var sourceOwnerType: OwnerType?
        get() = sender.ownerType
        set(value) {
            sender.ownerType = value
        }

    var sourceOwnerName: String?
        get() = sender.ownerName
        set(value) {
            sender.ownerName = value
        }

    var targetOwnerId: String?
        get() = receiver.ownerId
        set(value) {
            receiver.ownerId = value
        }

    var targetOwnerType: OwnerType?
        get() = receiver.ownerType
        set(value) {
            receiver.ownerType = value
        }

    var targetOwnerName: String?
        get() = receiver.ownerName
        set(value) {
            receiver.ownerName = value
        }

    companion object {
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
            updatedAt: Instant,
            normalizedDescription: String? = null,
            transactionCategory: String? = null,
            merchantName: String? = null,
            merchantCategory: String? = null,
            geoCountry: String? = null,
            geoCity: String? = null,
            riskScore: Int? = null,
            riskLevel: String? = null,
            tags: List<String> = emptyList(),
        ) = TransactionDTO(
            id = id,
            reference = reference,
            type = TransactionType.valueOf(type),
            status = TransactionStatus.valueOf(status),
            sender = PartyDTO(walletId = sourceWalletId),
            receiver = PartyDTO(walletId = targetWalletId),
            amount = MoneyDTO(amount = amount, currency = currency),
            createdAt = createdAt,
            updatedAt = updatedAt,
            description = description,
            category = transactionCategory,
            merchant = if (merchantName == null && merchantCategory == null) null else MerchantDTO(
                name = merchantName,
                category = merchantCategory,
            ),
            location = if (geoCountry == null && geoCity == null) null else LocationDTO(
                country = geoCountry,
                city = geoCity,
            ),
            risk = if (riskScore == null && riskLevel == null) null else RiskDTO(
                score = riskScore,
                level = riskLevel,
            ),
            tags = tags,
            metadata = buildMap {
                if (normalizedDescription != null) {
                    put("normalizedDescription", normalizedDescription)
                }
            },
        )
    }
}

data class PartyDTO(
    val walletId: String?,
    var ownerId: String? = null,
    var ownerType: OwnerType? = null,
    var ownerName: String? = null,
)

data class MoneyDTO(
    val amount: BigDecimal,
    val currency: String,
)

data class MerchantDTO(
    val name: String?,
    val category: String?,
)

data class LocationDTO(
    val country: String?,
    val city: String?,
)

data class RiskDTO(
    val score: Int?,
    val level: String?,
)
