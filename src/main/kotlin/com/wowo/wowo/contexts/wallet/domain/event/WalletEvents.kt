package com.wowo.wowo.contexts.wallet.domain.event

import com.wowo.wowo.shared.domain.BaseDomainEvent
import com.wowo.wowo.shared.valueobject.Currency

class WalletCreatedEvent(
    aggregateId: String,
    val userId: String,
    val currency: Currency
) : BaseDomainEvent(aggregateId)

class WalletCreditedEvent(
    aggregateId: String,
    val userId: String,
    val amount: java.math.BigDecimal,
    val currency: Currency
) : BaseDomainEvent(aggregateId)

class WalletDebitedEvent(
    aggregateId: String,
    val userId: String,
    val amount: java.math.BigDecimal,
    val currency: Currency
) : BaseDomainEvent(aggregateId)

