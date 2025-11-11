package com.veidz.financeapi.domain.entities;

import com.veidz.financeapi.domain.valueobjects.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FinancialGoalTest {

  @Test
  void shouldCreateFinancialGoalWithValidData() {
    UUID userId = UUID.randomUUID();
    String name = "Emergency Fund";
    Money targetAmount = new Money(new BigDecimal("10000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(12);

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);

    assertNotNull(goal);
    assertNotNull(goal.getId());
    assertEquals(userId, goal.getUserId());
    assertEquals(name, goal.getName());
    assertEquals(targetAmount, goal.getTargetAmount());
    assertEquals(deadline, goal.getDeadline());
    assertNotNull(goal.getCreatedAt());
    assertNotNull(goal.getUpdatedAt());
  }

  @Test
  void shouldThrowExceptionWhenUserIdIsNull() {
    String name = "Vacation Fund";
    Money targetAmount = new Money(new BigDecimal("5000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(6);

    assertThrows(IllegalArgumentException.class,
        () -> FinancialGoal.create(null, name, targetAmount, deadline));
  }

  @Test
  void shouldThrowExceptionWhenNameIsNull() {
    UUID userId = UUID.randomUUID();
    Money targetAmount = new Money(new BigDecimal("5000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(6);

    assertThrows(IllegalArgumentException.class,
        () -> FinancialGoal.create(userId, null, targetAmount, deadline));
  }

  @Test
  void shouldThrowExceptionWhenNameIsEmpty() {
    UUID userId = UUID.randomUUID();
    Money targetAmount = new Money(new BigDecimal("5000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(6);

    assertThrows(IllegalArgumentException.class,
        () -> FinancialGoal.create(userId, "", targetAmount, deadline));
  }

  @Test
  void shouldThrowExceptionWhenNameIsBlank() {
    UUID userId = UUID.randomUUID();
    Money targetAmount = new Money(new BigDecimal("5000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(6);

    assertThrows(IllegalArgumentException.class,
        () -> FinancialGoal.create(userId, "   ", targetAmount, deadline));
  }

  @Test
  void shouldThrowExceptionWhenTargetAmountIsNull() {
    UUID userId = UUID.randomUUID();
    String name = "New Car";
    LocalDate deadline = LocalDate.now().plusMonths(24);

    assertThrows(IllegalArgumentException.class,
        () -> FinancialGoal.create(userId, name, null, deadline));
  }

  @Test
  void shouldThrowExceptionWhenDeadlineIsNull() {
    UUID userId = UUID.randomUUID();
    String name = "House Down Payment";
    Money targetAmount = new Money(new BigDecimal("50000.00"), Currency.getInstance("BRL"));

    assertThrows(IllegalArgumentException.class,
        () -> FinancialGoal.create(userId, name, targetAmount, null));
  }

  @Test
  void shouldThrowExceptionWhenDeadlineIsInThePast() {
    UUID userId = UUID.randomUUID();
    String name = "Investment";
    Money targetAmount = new Money(new BigDecimal("10000.00"), Currency.getInstance("BRL"));
    LocalDate pastDate = LocalDate.now().minusDays(1);

    assertThrows(IllegalArgumentException.class,
        () -> FinancialGoal.create(userId, name, targetAmount, pastDate));
  }

  @Test
  void shouldAllowDeadlineToBeToday() {
    UUID userId = UUID.randomUUID();
    String name = "Short Term Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate today = LocalDate.now();

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, today);

    assertNotNull(goal);
    assertEquals(today, goal.getDeadline());
  }

  @Test
  void shouldCalculateProgressPercentage() {
    UUID userId = UUID.randomUUID();
    String name = "Savings Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(6);
    Money currentAmount = new Money(new BigDecimal("250.00"), Currency.getInstance("BRL"));

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);
    BigDecimal progress = goal.calculateProgress(currentAmount);

    assertEquals(new BigDecimal("25.00"), progress);
  }

  @Test
  void shouldReturnZeroProgressWhenCurrentAmountIsZero() {
    UUID userId = UUID.randomUUID();
    String name = "Investment Goal";
    Money targetAmount = new Money(new BigDecimal("5000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(12);
    Money currentAmount = new Money(BigDecimal.ZERO, Currency.getInstance("BRL"));

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);
    BigDecimal progress = goal.calculateProgress(currentAmount);

    assertEquals(BigDecimal.ZERO, progress);
  }

  @Test
  void shouldReturnFullProgressWhenGoalIsReached() {
    UUID userId = UUID.randomUUID();
    String name = "Completed Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(3);
    Money currentAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);
    BigDecimal progress = goal.calculateProgress(currentAmount);

    assertEquals(new BigDecimal("100.00"), progress);
  }

  @Test
  void shouldAllowProgressOverOneHundredPercent() {
    UUID userId = UUID.randomUUID();
    String name = "Exceeded Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(3);
    Money currentAmount = new Money(new BigDecimal("1500.00"), Currency.getInstance("BRL"));

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);
    BigDecimal progress = goal.calculateProgress(currentAmount);

    assertEquals(new BigDecimal("150.00"), progress);
  }

  @Test
  void shouldThrowExceptionWhenCalculatingProgressWithNullAmount() {
    UUID userId = UUID.randomUUID();
    String name = "Test Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(6);

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);

    assertThrows(IllegalArgumentException.class, () -> goal.calculateProgress(null));
  }

  @Test
  void shouldThrowExceptionWhenCalculatingProgressWithDifferentCurrency() {
    UUID userId = UUID.randomUUID();
    String name = "BRL Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(6);
    Money usdAmount = new Money(new BigDecimal("200.00"), Currency.getInstance("USD"));

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);

    assertThrows(IllegalArgumentException.class, () -> goal.calculateProgress(usdAmount));
  }

  @Test
  void shouldCalculateRemainingAmount() {
    UUID userId = UUID.randomUUID();
    String name = "Home Renovation";
    Money targetAmount = new Money(new BigDecimal("10000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(12);
    Money currentAmount = new Money(new BigDecimal("3000.00"), Currency.getInstance("BRL"));

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);
    Money remaining = goal.calculateRemaining(currentAmount);

    assertEquals(new Money(new BigDecimal("7000.00"), Currency.getInstance("BRL")), remaining);
  }

  @Test
  void shouldReturnZeroRemainingWhenGoalIsReached() {
    UUID userId = UUID.randomUUID();
    String name = "Reached Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(3);
    Money currentAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);
    Money remaining = goal.calculateRemaining(currentAmount);

    assertEquals(new Money(BigDecimal.ZERO, Currency.getInstance("BRL")), remaining);
  }

  @Test
  void shouldReturnZeroRemainingWhenGoalIsExceeded() {
    UUID userId = UUID.randomUUID();
    String name = "Exceeded Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(3);
    Money currentAmount = new Money(new BigDecimal("1500.00"), Currency.getInstance("BRL"));

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);
    Money remaining = goal.calculateRemaining(currentAmount);

    assertEquals(new Money(BigDecimal.ZERO, Currency.getInstance("BRL")), remaining);
  }

  @Test
  void shouldThrowExceptionWhenCalculatingRemainingWithNullAmount() {
    UUID userId = UUID.randomUUID();
    String name = "Test Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(6);

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);

    assertThrows(IllegalArgumentException.class, () -> goal.calculateRemaining(null));
  }

  @Test
  void shouldThrowExceptionWhenCalculatingRemainingWithDifferentCurrency() {
    UUID userId = UUID.randomUUID();
    String name = "BRL Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(6);
    Money eurAmount = new Money(new BigDecimal("200.00"), Currency.getInstance("EUR"));

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);

    assertThrows(IllegalArgumentException.class, () -> goal.calculateRemaining(eurAmount));
  }

  @Test
  void shouldCheckIfGoalIsReached() {
    UUID userId = UUID.randomUUID();
    String name = "Completed Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(3);
    Money currentAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);

    assertTrue(goal.isReached(currentAmount));
  }

  @Test
  void shouldCheckIfGoalIsNotReached() {
    UUID userId = UUID.randomUUID();
    String name = "In Progress Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(3);
    Money currentAmount = new Money(new BigDecimal("500.00"), Currency.getInstance("BRL"));

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);

    assertFalse(goal.isReached(currentAmount));
  }

  @Test
  void shouldConsiderGoalReachedWhenExceeded() {
    UUID userId = UUID.randomUUID();
    String name = "Exceeded Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(3);
    Money currentAmount = new Money(new BigDecimal("1500.00"), Currency.getInstance("BRL"));

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);

    assertTrue(goal.isReached(currentAmount));
  }

  @Test
  void shouldCheckIfDeadlineHasPassed() {
    UUID userId = UUID.randomUUID();
    String name = "Past Deadline Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate yesterday = LocalDate.now().minusDays(1);

    // This should fail in create, but let's test isDeadlinePassed for future deadline
    LocalDate futureDeadline = LocalDate.now().plusDays(1);
    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, futureDeadline);

    assertFalse(goal.isDeadlinePassed());
  }

  @Test
  void shouldCheckIfDeadlineIsToday() {
    UUID userId = UUID.randomUUID();
    String name = "Today Deadline Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate today = LocalDate.now();

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, today);

    assertFalse(goal.isDeadlinePassed());
  }

  @Test
  void shouldUpdateName() {
    UUID userId = UUID.randomUUID();
    String originalName = "Original Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(6);

    FinancialGoal goal = FinancialGoal.create(userId, originalName, targetAmount, deadline);
    String newName = "Updated Goal Name";

    goal.updateName(newName);

    assertEquals(newName, goal.getName());
  }

  @Test
  void shouldThrowExceptionWhenUpdatingToNullName() {
    UUID userId = UUID.randomUUID();
    String name = "Valid Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(6);

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);

    assertThrows(IllegalArgumentException.class, () -> goal.updateName(null));
  }

  @Test
  void shouldThrowExceptionWhenUpdatingToEmptyName() {
    UUID userId = UUID.randomUUID();
    String name = "Valid Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(6);

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);

    assertThrows(IllegalArgumentException.class, () -> goal.updateName(""));
  }

  @Test
  void shouldUpdateTargetAmount() {
    UUID userId = UUID.randomUUID();
    String name = "Flexible Goal";
    Money originalTarget = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(6);

    FinancialGoal goal = FinancialGoal.create(userId, name, originalTarget, deadline);
    Money newTarget = new Money(new BigDecimal("2000.00"), Currency.getInstance("BRL"));

    goal.updateTargetAmount(newTarget);

    assertEquals(newTarget, goal.getTargetAmount());
  }

  @Test
  void shouldThrowExceptionWhenUpdatingToNullTargetAmount() {
    UUID userId = UUID.randomUUID();
    String name = "Valid Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(6);

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);

    assertThrows(IllegalArgumentException.class, () -> goal.updateTargetAmount(null));
  }

  @Test
  void shouldUpdateDeadline() {
    UUID userId = UUID.randomUUID();
    String name = "Adjustable Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate originalDeadline = LocalDate.now().plusMonths(6);

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, originalDeadline);
    LocalDate newDeadline = LocalDate.now().plusMonths(12);

    goal.updateDeadline(newDeadline);

    assertEquals(newDeadline, goal.getDeadline());
  }

  @Test
  void shouldThrowExceptionWhenUpdatingToNullDeadline() {
    UUID userId = UUID.randomUUID();
    String name = "Valid Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(6);

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);

    assertThrows(IllegalArgumentException.class, () -> goal.updateDeadline(null));
  }

  @Test
  void shouldThrowExceptionWhenUpdatingToPastDeadline() {
    UUID userId = UUID.randomUUID();
    String name = "Valid Goal";
    Money targetAmount = new Money(new BigDecimal("1000.00"), Currency.getInstance("BRL"));
    LocalDate deadline = LocalDate.now().plusMonths(6);

    FinancialGoal goal = FinancialGoal.create(userId, name, targetAmount, deadline);
    LocalDate pastDate = LocalDate.now().minusDays(1);

    assertThrows(IllegalArgumentException.class, () -> goal.updateDeadline(pastDate));
  }
}
