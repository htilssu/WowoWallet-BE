package com.wowo.wowo.contexts.wallet.infrastructure.persistence

import com.wowo.wowo.shared.valueobject.Currency
import com.wowo.wowo.shared.domain.OwnerType
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant

import java.time.LocalDateTime
import java.util.*

/**
 * JPA Entity for Wallet
 */
@Entity
@Table(
    name = "wallets", indexes = [Index(name = "idx_wallets_owner_id_currency", columnList = "ownerId, currency")]
)
class WalletJpaEntity(
    @Id var id: UUID = UUID.randomUUID(),

    @Column(nullable = false) var ownerId: String,

    @Column(nullable = false) @Enumerated(EnumType.STRING) var ownerType: OwnerType,

    @Column(nullable = false, precision = 19, scale = 2)

    var balance: BigDecimal,

    @Column(nullable = false, length = 3) @Enumerated(EnumType.STRING) var currency: Currency,

    @Column(nullable = false) var isActive: Boolean = true,

    @Column(nullable = false, updatable = false) var createdAt: Instant = Instant.now(),

    @Column(nullable = false) var updatedAt: Instant = Instant.now()
)

