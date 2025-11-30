package com.wowo.wowo.domain.valueobjects;
}
    }
        assertThrows(BadRequest.class, () -> vnd.add(usd));

        Money usd = new Money(100.0, "USD");
        Money vnd = new Money(100.0, "VND");
    void shouldHandleDifferentCurrencies() {
    @Test

    }
        assertEquals(150.0, modified.toDouble(), 0.01);
        assertEquals(100.0, original.toDouble(), 0.01);

        Money modified = original.add(new Money(50.0));
        Money original = new Money(100.0);
    void shouldBeImmutable() {
    @Test

    }
        assertEquals(0.0, zero.toDouble());
        assertTrue(zero.isZero());

        Money zero = Money.zero();
    void shouldReturnZeroMoney() {
    @Test

    }
        assertEquals(150.0, result.toDouble(), 0.01);

        Money result = money.multiply(1.5);

        Money money = new Money(100.0);
    void shouldMultiplyMoneyCorrectly() {
    @Test

    }
        assertTrue(money2.isLessThan(money1));
        assertTrue(money1.isGreaterThanOrEqual(money2));
        assertFalse(money2.isGreaterThan(money1));
        assertTrue(money1.isGreaterThan(money2));

        Money money2 = new Money(50.0);
        Money money1 = new Money(100.0);
    void shouldCompareMoneyCorrectly() {
    @Test

    }
        assertThrows(BadRequest.class, () -> money1.subtract(money2));

        Money money2 = new Money(100.0);
        Money money1 = new Money(50.0);
    void shouldThrowExceptionWhenSubtractResultsInNegative() {
    @Test

    }
        assertEquals(50.0, result.toDouble(), 0.01);

        Money result = money1.subtract(money2);

        Money money2 = new Money(50.0);
        Money money1 = new Money(100.0);
    void shouldSubtractMoneyCorrectly() {
    @Test

    }
        assertEquals(150.0, result.toDouble(), 0.01);

        Money result = money1.add(money2);

        Money money2 = new Money(50.0);
        Money money1 = new Money(100.0);
    void shouldAddMoneyCorrectly() {
    @Test

    }
        assertThrows(BadRequest.class, () -> new Money(null, "VND"));
    void shouldThrowExceptionForNullAmount() {
    @Test

    }
        assertThrows(BadRequest.class, () -> new Money(-100.0));
    void shouldThrowExceptionForNegativeAmount() {
    @Test

    }
        assertEquals("VND", money.getCurrency());
        assertEquals(100.50, money.toDouble(), 0.01);
        Money money = new Money(100.50);
    void shouldCreateMoneyWithPositiveAmount() {
    @Test

class MoneyTest {
 */
 * Unit tests for Money value object
/**

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import com.wowo.wowo.exception.BadRequest;


