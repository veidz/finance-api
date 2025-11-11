package com.veidz.financeapi.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.veidz.financeapi.domain.valueobjects.DateRange;
import com.veidz.financeapi.domain.valueobjects.Money;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Budget entity.
 *
 * @author Veidz
 */
class BudgetTest {

  @Test
  void shouldCreateBudgetWithRequiredFields() {
    // Given
    UUID userId = UUID.randomUUID();
    String name = "Monthly Groceries";
    Money amount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));

    // When
    Budget budget = Budget.create(userId, name, amount, period);

    // Then
    assertNotNull(budget);
    assertNotNull(budget.getId());
    assertEquals(userId, budget.getUserId());
    assertEquals(name, budget.getName());
    assertEquals(amount, budget.getAmount());
    assertEquals(period, budget.getPeriod());
    assertNull(budget.getCategoryId());
    assertNotNull(budget.getCreatedAt());
  }

  @Test
  void shouldCreateBudgetWithCategory() {
    // Given
    UUID userId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    String name = "Food Budget";
    Money amount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));

    // When
    Budget budget = Budget.create(userId, name, amount, period, categoryId);

    // Then
    assertEquals(categoryId, budget.getCategoryId());
  }

  @Test
  void shouldThrowExceptionWhenUserIdIsNull() {
    // Given
    String name = "Test Budget";
    Money amount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> Budget.create(null, name, amount, period));
  }

  @Test
  void shouldThrowExceptionWhenNameIsNull() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> Budget.create(userId, null, amount, period));
  }

  @Test
  void shouldThrowExceptionWhenNameIsEmpty() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> Budget.create(userId, "", amount, period));
  }

  @Test
  void shouldThrowExceptionWhenNameIsBlank() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));

    // When & Then
    assertThrows(IllegalArgumentException.class,
        () -> Budget.create(userId, "   ", amount, period));
  }

  @Test
  void shouldThrowExceptionWhenAmountIsNull() {
    // Given
    UUID userId = UUID.randomUUID();
    String name = "Test Budget";
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> Budget.create(userId, name, null, period));
  }

  @Test
  void shouldThrowExceptionWhenAmountIsZero() {
    // Given
    UUID userId = UUID.randomUUID();
    String name = "Test Budget";
    Money amount = Money.zero(Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> Budget.create(userId, name, amount, period));
  }

  @Test
  void shouldThrowExceptionWhenPeriodIsNull() {
    // Given
    UUID userId = UUID.randomUUID();
    String name = "Test Budget";
    Money amount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> Budget.create(userId, name, amount, null));
  }

  @Test
  void shouldTrimBudgetName() {
    // Given
    UUID userId = UUID.randomUUID();
    String name = "  Monthly Budget  ";
    Money amount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));

    // When
    Budget budget = Budget.create(userId, name, amount, period);

    // Then
    assertEquals("Monthly Budget", budget.getName());
  }

  @Test
  void shouldUpdateBudgetName() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));
    Budget budget = Budget.create(userId, "Old Name", amount, period);

    // When
    budget.updateName("New Name");

    // Then
    assertEquals("New Name", budget.getName());
  }

  @Test
  void shouldThrowExceptionWhenUpdatingNameToNull() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));
    Budget budget = Budget.create(userId, "Old Name", amount, period);

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> budget.updateName(null));
  }

  @Test
  void shouldUpdateBudgetAmount() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));
    Budget budget = Budget.create(userId, "Monthly Budget", amount, period);

    Money newAmount = new Money(BigDecimal.valueOf(750.00), Currency.getInstance("USD"));

    // When
    budget.updateAmount(newAmount);

    // Then
    assertEquals(newAmount, budget.getAmount());
  }

  @Test
  void shouldThrowExceptionWhenUpdatingAmountToNull() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));
    Budget budget = Budget.create(userId, "Monthly Budget", amount, period);

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> budget.updateAmount(null));
  }

  @Test
  void shouldThrowExceptionWhenUpdatingAmountToZero() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));
    Budget budget = Budget.create(userId, "Monthly Budget", amount, period);

    Money zeroAmount = Money.zero(Currency.getInstance("USD"));

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> budget.updateAmount(zeroAmount));
  }

  @Test
  void shouldSetCategoryId() {
    // Given
    UUID userId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));
    Budget budget = Budget.create(userId, "Monthly Budget", amount, period);

    // When
    budget.setCategoryId(categoryId);

    // Then
    assertEquals(categoryId, budget.getCategoryId());
  }

  @Test
  void shouldAllowNullCategoryId() {
    // Given
    UUID userId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));
    Budget budget = Budget.create(userId, "Monthly Budget", amount, period, categoryId);

    // When
    budget.setCategoryId(null);

    // Then
    assertNull(budget.getCategoryId());
  }

  @Test
  void shouldCheckIfBudgetIsActive() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange activePeriod = new DateRange(LocalDate.now().minusDays(5),
        LocalDate.now().plusDays(5));
    Budget activeBudget = Budget.create(userId, "Active Budget", amount, activePeriod);

    DateRange pastPeriod = new DateRange(LocalDate.now().minusDays(30),
        LocalDate.now().minusDays(1));
    Budget pastBudget = Budget.create(userId, "Past Budget", amount, pastPeriod);

    DateRange futurePeriod = new DateRange(LocalDate.now().plusDays(1),
        LocalDate.now().plusDays(30));
    Budget futureBudget = Budget.create(userId, "Future Budget", amount, futurePeriod);

    // Then
    assertTrue(activeBudget.isActive());
    assertFalse(pastBudget.isActive());
    assertFalse(futureBudget.isActive());
  }

  @Test
  void shouldCalculateRemainingAmount() {
    // Given
    UUID userId = UUID.randomUUID();
    Money budgetAmount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));
    Budget budget = Budget.create(userId, "Monthly Budget", budgetAmount, period);

    Money spent = new Money(BigDecimal.valueOf(200.00), Currency.getInstance("USD"));

    // When
    BigDecimal remaining = budget.calculateRemaining(spent);

    // Then
    assertEquals(0, remaining.compareTo(BigDecimal.valueOf(300.00)));
  }

  @Test
  void shouldCalculateRemainingAmountWhenOverBudget() {
    // Given
    UUID userId = UUID.randomUUID();
    Money budgetAmount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));
    Budget budget = Budget.create(userId, "Monthly Budget", budgetAmount, period);

    Money spent = new Money(BigDecimal.valueOf(600.00), Currency.getInstance("USD"));

    // When
    BigDecimal remaining = budget.calculateRemaining(spent);

    // Then
    assertEquals(0, remaining.compareTo(BigDecimal.valueOf(-100.00)));
  }

  @Test
  void shouldThrowExceptionWhenCalculatingRemainingWithDifferentCurrency() {
    // Given
    UUID userId = UUID.randomUUID();
    Money budgetAmount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));
    Budget budget = Budget.create(userId, "Monthly Budget", budgetAmount, period);

    Money spentEur = new Money(BigDecimal.valueOf(200.00), Currency.getInstance("EUR"));

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> budget.calculateRemaining(spentEur));
  }

  @Test
  void shouldCalculatePercentageUsed() {
    // Given
    UUID userId = UUID.randomUUID();
    Money budgetAmount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));
    Budget budget = Budget.create(userId, "Monthly Budget", budgetAmount, period);

    Money spent = new Money(BigDecimal.valueOf(250.00), Currency.getInstance("USD"));

    // When
    BigDecimal percentage = budget.calculatePercentageUsed(spent);

    // Then
    assertEquals(0, percentage.compareTo(BigDecimal.valueOf(50.00)));
  }

  @Test
  void shouldCalculatePercentageUsedWhenOverBudget() {
    // Given
    UUID userId = UUID.randomUUID();
    Money budgetAmount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));
    Budget budget = Budget.create(userId, "Monthly Budget", budgetAmount, period);

    Money spent = new Money(BigDecimal.valueOf(750.00), Currency.getInstance("USD"));

    // When
    BigDecimal percentage = budget.calculatePercentageUsed(spent);

    // Then
    assertEquals(0, percentage.compareTo(BigDecimal.valueOf(150.00)));
  }

  @Test
  void shouldCheckIfBudgetIsExceeded() {
    // Given
    UUID userId = UUID.randomUUID();
    Money budgetAmount = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));
    Budget budget = Budget.create(userId, "Monthly Budget", budgetAmount, period);

    Money underSpent = new Money(BigDecimal.valueOf(400.00), Currency.getInstance("USD"));
    Money exactSpent = new Money(BigDecimal.valueOf(500.00), Currency.getInstance("USD"));
    Money overSpent = new Money(BigDecimal.valueOf(600.00), Currency.getInstance("USD"));

    // Then
    assertFalse(budget.isExceeded(underSpent));
    assertFalse(budget.isExceeded(exactSpent));
    assertTrue(budget.isExceeded(overSpent));
  }
}
