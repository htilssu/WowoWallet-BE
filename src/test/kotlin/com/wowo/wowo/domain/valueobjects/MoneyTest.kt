package com.wowo.wowo.domain.valueobjects

import com.wowo.wowo.shared.valueobject.Money
import com.wowo.wowo.shared.valueobject.Currency
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.math.BigDecimal

/**
 * Unit tests for Money value object
 */
class MoneyTest {

    @Test
    fun `should create money with amount`() {
        val money = Money(BigDecimal("100.50"), Currency.VND)
        assertEquals(BigDecimal("100.50"), money.amount)
        assertEquals(Currency.VND, money.currency)
    }

    @Test
    fun `should add money correctly`() {
        val money1 = Money(BigDecimal("100.00"), Currency.VND)
        val money2 = Money(BigDecimal("50.00"), Currency.VND)

        val result = money1.add(money2)

        assertEquals(BigDecimal("150.00"), result.amount)
    }

    @Test
    fun `should subtract money correctly`() {
        val money1 = Money(BigDecimal("100.00"), Currency.VND)
        val money2 = Money(BigDecimal("50.00"), Currency.VND)

        val result = money1.subtract(money2)

        assertEquals(BigDecimal("50.00"), result.amount)
    }

    @Test
    fun `should multiply money correctly`() {
        val money = Money(BigDecimal("100.00"), Currency.VND)

        val result = money.multiply(BigDecimal("1.5"))

        assertEquals(BigDecimal("150.00"), result.amount)
    }

    @Test
    fun `should return zero money`() {
        val zero = Money.zero(Currency.VND)

        assertTrue(zero.isZero())
    }

    @Test
    fun `should throw exception for different currencies`() {
        val vnd = Money(BigDecimal("100.00"), Currency.VND)
        val usd = Money(BigDecimal("100.00"), Currency.USD)

        assertThrows(IllegalArgumentException::class.java) { vnd.add(usd) }
    }
}

