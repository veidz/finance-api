package com.veidz.financeapi.domain.valueobjects;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

/**
 * Value Object representing money with amount and currency.
 * Immutable and ensures business invariants.
 *
 * @author Veidz
 */
public final class Money {

  private final BigDecimal amount;
  private final Currency currency;

  /**
   * Creates a Money instance with the given amount and currency.
   *
   * @param amount the monetary amount (must be non-null and non-negative)
   * @param currency the currency (must be non-null)
   * @throws IllegalArgumentException if amount or currency is null, or if amount is negative
   */
  public Money(BigDecimal amount, Currency currency) {
    if (amount == null) {
      throw new IllegalArgumentException("Amount cannot be null");
    }
    if (currency == null) {
      throw new IllegalArgumentException("Currency cannot be null");
    }
    if (amount.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }
    this.amount = amount;
    this.currency = currency;
  }

  /**
   * Creates a Money instance with zero amount for the given currency.
   *
   * @param currency the currency
   * @return a Money instance with zero amount
   */
  public static Money zero(Currency currency) {
    return new Money(BigDecimal.ZERO, currency);
  }

  /**
   * Adds another Money to this Money.
   *
   * @param other the Money to add
   * @return a new Money instance with the sum
   * @throws IllegalArgumentException if currencies differ
   */
  public Money add(Money other) {
    if (!this.currency.equals(other.currency)) {
      throw new IllegalArgumentException("Cannot add money with different currencies");
    }
    return new Money(this.amount.add(other.amount), this.currency);
  }

  /**
   * Subtracts another Money from this Money.
   *
   * @param other the Money to subtract
   * @return a new Money instance with the difference
   * @throws IllegalArgumentException if currencies differ
   */
  public Money subtract(Money other) {
    if (!this.currency.equals(other.currency)) {
      throw new IllegalArgumentException("Cannot subtract money with different currencies");
    }
    return new Money(this.amount.subtract(other.amount), this.currency);
  }

  /**
   * Checks if this Money is greater than another Money.
   *
   * @param other the Money to compare
   * @return true if this Money is greater
   */
  public boolean isGreaterThan(Money other) {
    if (!this.currency.equals(other.currency)) {
      throw new IllegalArgumentException("Cannot compare money with different currencies");
    }
    return this.amount.compareTo(other.amount) > 0;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Currency getCurrency() {
    return currency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Money money = (Money) o;
    return amount.compareTo(money.amount) == 0 && Objects.equals(currency, money.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, currency);
  }

  @Override
  public String toString() {
    return currency.getSymbol() + " " + amount;
  }
}
