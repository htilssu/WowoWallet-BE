package com.wowo.wowo.contexts.wallet.domain.event

import com.wowo.wowo.shared.domain.BaseDomainEvent
import com.wowo.wowo.shared.valueobject.Currency
import com.wowo.wowo.contexts.wallet.domain.valueobject.OwnerType

class WalletCreatedEvent(
    aggregateId: String,
    val ownerId: String,
    val ownerType: OwnerType,
    val currency: Currency
) : BaseDomainEvent(aggregateId)

class WalletCreditedEvent(
    aggregateId: String,
    val ownerId: String,
    val ownerType: OwnerType,
    val amount: java.math.BigDecimal,
    val currency: Currency
) : BaseDomainEvent(aggregateId)

class WalletDebitedEvent(
    aggregateId: String,
    val ownerId: String,
    val ownerType: OwnerType,
    val amount: java.math.BigDecimal,
    val currency: Currency
) : BaseDomainEvent(aggregateId)


