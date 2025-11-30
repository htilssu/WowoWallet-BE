package com.wowo.wowo.contexts.transaction.infrastructure.persistence

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

/**
 * JPA Entity for Transaction
 */
@Entity
@Table(name = "transactions")
class TransactionJpaEntity(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column
    var fromWalletId: String? = null,

    @Column
    var toWalletId: String? = null,

    @Column(nullable = false, precision = 19, scale = 2)
    var amount: BigDecimal,

    @Column(nullable = false, length = 3)
    var currency: String,

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    var type: String,

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    var status: String,

    @Column(length = 500)
    var description: String? = null,

    @Column
    var reference: String? = null,

    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)

