package com.veidz.financeapi.domain.entities;

import com.veidz.financeapi.domain.valueobjects.DateRange;
import com.veidz.financeapi.domain.valueobjects.Money;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Budget domain entity representing a financial budget in the system. Contains business logic and
 * invariants for budget management, including period tracking and spending calculations.
 *
 * @author Veidz
 */
public final class Budget {

  private final UUID id;
  private final UUID userId;
  private String name;
  private Money amount;
  private final DateRange period;
  private UUID categoryId;
  private final LocalDateTime createdAt;

  /**
   * Private constructor to prevent direct instantiation. Use static factory method instead.
   */
  private Budget(UUID id, UUID userId, String name, Money amount, DateRange period, UUID categoryId,
      LocalDateTime createdAt) {
    this.id = id;
    this.userId = userId;
    this.name = name;
    this.amount = amount;
    this.period = period;
    this.categoryId = categoryId;
    this.createdAt = createdAt;
  }

  /**
   * Creates a new Budget with the given details (without category).
   *
   * @param userId the user ID who owns this budget (must be non-null)
   * @param name the budget name (must be non-null and non-empty)
   * @param amount the budget amount limit (must be non-null and positive)
   * @param period the date range for this budget (must be non-null)
   * @return a new Budget instance
   * @throws IllegalArgumentException if any validation fails
   */
  public static Budget create(UUID userId, String name, Money amount, DateRange period) {
    return create(userId, name, amount, period, null);
  }

  /**
   * Creates a new Budget with the given details (with optional category).
   *
   * @param userId the user ID who owns this budget (must be non-null)
   * @param name the budget name (must be non-null and non-empty)
   * @param amount the budget amount limit (must be non-null and positive)
   * @param period the date range for this budget (must be non-null)
   * @param categoryId the category ID for this budget (can be null)
   * @return a new Budget instance
   * @throws IllegalArgumentException if any validation fails
   */
  public static Budget create(UUID userId, String name, Money amount, DateRange period,
      UUID categoryId) {
    validateUserId(userId);
    validateName(name);
    validateAmount(amount);
    validatePeriod(period);

    return new Budget(UUID.randomUUID(), userId, name.trim(), amount, period, categoryId,
        LocalDateTime.now());
  }

  /**
   * Updates the budget name.
   *
   * @param newName the new name (must be non-null and non-empty)
   * @throws IllegalArgumentException if name is invalid
   */
  public void updateName(String newName) {
    validateName(newName);
    this.name = newName.trim();
  }

  /**
   * Updates the budget amount limit.
   *
   * @param newAmount the new amount (must be non-null and positive)
   * @throws IllegalArgumentException if amount is invalid
   */
  public void updateAmount(Money newAmount) {
    validateAmount(newAmount);
    this.amount = newAmount;
  }

  /**
   * Sets the category ID for this budget.
   *
   * @param categoryId the category ID (can be null)
   */
  public void setCategoryId(UUID categoryId) {
    this.categoryId = categoryId;
  }

  /**
   * Checks if this budget is currently active (current date falls within the period).
   *
   * @return true if budget is active, false otherwise
   */
  public boolean isActive() {
    return period.contains(LocalDate.now());
  }

  /**
   * Calculates the remaining amount in the budget after spending. Returns a BigDecimal that can be
   * negative if over budget.
   *
   * @param spent the amount spent (must have same currency as budget)
   * @return the remaining amount as BigDecimal (can be negative if over budget)
   * @throws IllegalArgumentException if currencies don't match
   */
  public BigDecimal calculateRemaining(Money spent) {
    if (spent == null) {
      throw new IllegalArgumentException("Spent amount cannot be null");
    }
    if (!spent.getCurrency().equals(amount.getCurrency())) {
      throw new IllegalArgumentException("Spent currency must match budget currency");
    }
    return amount.getAmount().subtract(spent.getAmount());
  }

  /**
   * Calculates the percentage of budget used based on spending.
   *
   * @param spent the amount spent
   * @return the percentage used (can exceed 100 if over budget)
   * @throws IllegalArgumentException if currencies don't match
   */
  public BigDecimal calculatePercentageUsed(Money spent) {
    if (spent == null) {
      throw new IllegalArgumentException("Spent amount cannot be null");
    }
    if (!spent.getCurrency().equals(amount.getCurrency())) {
      throw new IllegalArgumentException("Spent currency must match budget currency");
    }

    if (amount.getAmount().compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    }

    return spent.getAmount().multiply(BigDecimal.valueOf(100)).divide(amount.getAmount(), 2,
        RoundingMode.HALF_UP);
  }

  /**
   * Checks if the budget has been exceeded.
   *
   * @param spent the amount spent
   * @return true if spending exceeds budget, false otherwise
   * @throws IllegalArgumentException if currencies don't match
   */
  public boolean isExceeded(Money spent) {
    if (spent == null) {
      throw new IllegalArgumentException("Spent amount cannot be null");
    }
    if (!spent.getCurrency().equals(amount.getCurrency())) {
      throw new IllegalArgumentException("Spent currency must match budget currency");
    }

    return spent.getAmount().compareTo(amount.getAmount()) > 0;
  }

  private static void validateUserId(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("User ID cannot be null");
    }
  }

  private static void validateName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Budget name cannot be null");
    }
    if (name.trim().isEmpty()) {
      throw new IllegalArgumentException("Budget name cannot be empty");
    }
  }

  private static void validateAmount(Money amount) {
    if (amount == null) {
      throw new IllegalArgumentException("Budget amount cannot be null");
    }
    if (amount.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Budget amount must be greater than zero");
    }
  }

  private static void validatePeriod(DateRange period) {
    if (period == null) {
      throw new IllegalArgumentException("Budget period cannot be null");
    }
  }

  public UUID getId() {
    return id;
  }

  public UUID getUserId() {
    return userId;
  }

  public String getName() {
    return name;
  }

  public Money getAmount() {
    return amount;
  }

  public DateRange getPeriod() {
    return period;
  }

  public UUID getCategoryId() {
    return categoryId;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Budget budget = (Budget) obj;
    return Objects.equals(id, budget.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Budget{" + "id=" + id + ", userId=" + userId + ", name='" + name + '\'' + ", amount="
        + amount + ", period=" + period + ", categoryId=" + categoryId + ", createdAt=" + createdAt
        + '}';
  }
}
