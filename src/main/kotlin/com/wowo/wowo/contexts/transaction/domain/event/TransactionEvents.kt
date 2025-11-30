package com.wowo.wowo.contexts.transaction.domain.event

import com.wowo.wowo.shared.domain.BaseDomainEvent
import com.wowo.wowo.shared.valueobject.Currency
import java.math.BigDecimal

class TransactionCompletedEvent(
    aggregateId: String,
    val fromWalletId: String?,
    val toWalletId: String?,
    val amount: BigDecimal,
    val currency: Currency
) : BaseDomainEvent(aggregateId)

class TransactionFailedEvent(
    aggregateId: String,
    val reason: String
) : BaseDomainEvent(aggregateId)

