package com.wowo.wowo.contexts.wallet.domain.valueobject

import com.wowo.wowo.shared.domain.ValueObject
import com.wowo.wowo.shared.valueobject.Money
import com.wowo.wowo.shared.valueobject.Currency
import java.math.BigDecimal

/**
 * Value Object representing wallet balance
 */
data class Balance(val money: Money) : ValueObject {

    fun add(amount: Money): Balance {
        return Balance(money.add(amount))
    }

    fun subtract(amount: Money): Balance {
        return Balance(money.subtract(amount))
    }

    fun isPositive(): Boolean = money.isPositive()
    fun isZero(): Boolean = money.isZero()

    companion object {
        fun zero(currency: Currency) = Balance(Money.zero(currency))
    }
}

