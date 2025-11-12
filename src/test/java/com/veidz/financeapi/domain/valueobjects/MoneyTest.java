package com.veidz.financeapi.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Money value object.
 * Following TDD: test first, then implement.
 *
 * @author Veidz
 */
class MoneyTest {

  @Test
  void shouldCreateValidMoneyWhenAmountAndCurrencyAreValid() {
    // Arrange
    BigDecimal amount = new BigDecimal("100.50");
    Currency currency = Currency.getInstance("BRL");

    // Act
    Money money = new Money(amount, currency);

    // Assert
    assertNotNull(money);
    assertEquals(amount, money.getAmount());
    assertEquals(currency, money.getCurrency());
  }

  @Test
  void shouldRejectNegativeAmountWhenCreatingMoney() {
    // Arrange
    BigDecimal negativeAmount = new BigDecimal("-10.00");
    Currency currency = Currency.getInstance("BRL");

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new Money(negativeAmount, currency)
    );
    assertEquals("Amount cannot be negative", exception.getMessage());
  }

  @Test
  void shouldRejectNullAmountWhenCreatingMoney() {
    // Arrange
    Currency currency = Currency.getInstance("BRL");

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new Money(null, currency)
    );
    assertEquals("Amount cannot be null", exception.getMessage());
  }

  @Test
  void shouldRejectNullCurrencyWhenCreatingMoney() {
    // Arrange
    BigDecimal amount = new BigDecimal("100.00");

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new Money(amount, null)
    );
    assertEquals("Currency cannot be null", exception.getMessage());
  }

  @Test
  void shouldAddTwoMoneyObjectsWithSameCurrency() {
    // Arrange
    Money money1 = new Money(new BigDecimal("100.00"), Currency.getInstance("BRL"));
    Money money2 = new Money(new BigDecimal("50.00"), Currency.getInstance("BRL"));

    // Act
    Money result = money1.add(money2);

    // Assert
    assertEquals(new BigDecimal("150.00"), result.getAmount());
    assertEquals(Currency.getInstance("BRL"), result.getCurrency());
  }

  @Test
  void shouldThrowExceptionWhenAddingMoneyWithDifferentCurrencies() {
    // Arrange
    Money moneyBRL = new Money(new BigDecimal("100.00"), Currency.getInstance("BRL"));
    Money moneyUSD = new Money(new BigDecimal("50.00"), Currency.getInstance("USD"));

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> moneyBRL.add(moneyUSD)
    );
    assertEquals("Cannot add money with different currencies", exception.getMessage());
  }

  @Test
  void shouldSubtractTwoMoneyObjectsWithSameCurrency() {
    // Arrange
    Money money1 = new Money(new BigDecimal("100.00"), Currency.getInstance("BRL"));
    Money money2 = new Money(new BigDecimal("30.00"), Currency.getInstance("BRL"));

    // Act
    Money result = money1.subtract(money2);

    // Assert
    assertEquals(new BigDecimal("70.00"), result.getAmount());
    assertEquals(Currency.getInstance("BRL"), result.getCurrency());
  }

  @Test
  void shouldThrowExceptionWhenSubtractingMoneyWithDifferentCurrencies() {
    // Arrange
    Money moneyBRL = new Money(new BigDecimal("100.00"), Currency.getInstance("BRL"));
    Money moneyUSD = new Money(new BigDecimal("50.00"), Currency.getInstance("USD"));

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> moneyBRL.subtract(moneyUSD)
    );
    assertEquals("Cannot subtract money with different currencies", exception.getMessage());
  }

  @Test
  void shouldCompareMoneyObjectsCorrectly() {
    // Arrange
    Money money1 = new Money(new BigDecimal("100.00"), Currency.getInstance("BRL"));
    Money money2 = new Money(new BigDecimal("100.00"), Currency.getInstance("BRL"));
    Money money3 = new Money(new BigDecimal("50.00"), Currency.getInstance("BRL"));

    // Act & Assert
    assertTrue(money1.isGreaterThan(money3));
    assertFalse(money3.isGreaterThan(money1));
    assertTrue(money1.equals(money2));
  }

  @Test
  void shouldReturnZeroMoneyWhenRequested() {
    // Arrange
    Currency currency = Currency.getInstance("BRL");

    // Act
    Money zeroMoney = Money.zero(currency);

    // Assert
    assertEquals(BigDecimal.ZERO, zeroMoney.getAmount());
    assertEquals(currency, zeroMoney.getCurrency());
  }
}
