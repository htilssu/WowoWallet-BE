package com.wowo.wowo.contexts.transaction.domain.repository

import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionType
import java.time.LocalDateTime

data class TransactionSearchCriteria(
    val walletId: String,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val type: TransactionType? = null,
    val page: Int = 0,
    val size: Int = 10
)
