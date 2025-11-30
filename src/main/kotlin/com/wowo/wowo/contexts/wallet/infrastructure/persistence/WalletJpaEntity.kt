package com.wowo.wowo.contexts.wallet.infrastructure.persistence

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

/**
 * JPA Entity for Wallet
 */
@Entity
@Table(name = "wallets")
class WalletJpaEntity(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var userId: String,

    @Column(nullable = false, precision = 19, scale = 2)
    var balance: BigDecimal,

    @Column(nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    var currency: String,

    @Column(nullable = false)
    var isActive: Boolean = true,

    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)

