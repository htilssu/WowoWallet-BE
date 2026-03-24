package com.wowo.wowo.contexts.transaction.domain.repository

import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionType
import java.time.Instant

data class TransactionSearchCriteria(
    val walletId: String,
    val startDate: Instant? = null,
    val endDate: Instant? = null,
    val type: TransactionType? = null,
    val page: Int = 0,
    val size: Int = 10
)
