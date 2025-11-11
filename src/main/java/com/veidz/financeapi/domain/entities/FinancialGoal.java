package com.veidz.financeapi.domain.entities;

import com.veidz.financeapi.domain.valueobjects.Money;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain entity representing a financial goal. Tracks target amount, deadline, and progress towards
 * achieving the goal.
 *
 * @author Veidz
 */
public final class FinancialGoal {

  private final UUID id;
  private final UUID userId;
  private String name;
  private Money targetAmount;
  private LocalDate deadline;
  private final LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private FinancialGoal(UUID id, UUID userId, String name, Money targetAmount, LocalDate deadline,
      LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.userId = userId;
    this.name = name;
    this.targetAmount = targetAmount;
    this.deadline = deadline;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  /**
   * Creates a new FinancialGoal with the specified parameters.
   *
   * @param userId the user ID who owns this goal
   * @param name the goal name
   * @param targetAmount the target amount to achieve
   * @param deadline the deadline to achieve the goal
   * @return a new FinancialGoal instance
   * @throws IllegalArgumentException if any parameter is invalid
   */
  public static FinancialGoal create(UUID userId, String name, Money targetAmount,
      LocalDate deadline) {
    validateUserId(userId);
    validateName(name);
    validateTargetAmount(targetAmount);
    validateDeadline(deadline);

    LocalDateTime now = LocalDateTime.now();
    return new FinancialGoal(UUID.randomUUID(), userId, name.trim(), targetAmount, deadline, now,
        now);
  }

  /**
   * Calculates the progress percentage towards the goal.
   *
   * @param currentAmount the current amount saved
   * @return the progress percentage (can be over 100%)
   * @throws IllegalArgumentException if currentAmount is null or has different currency
   */
  public BigDecimal calculateProgress(Money currentAmount) {
    validateCurrentAmount(currentAmount);

    if (currentAmount.getAmount().compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO;
    }

    BigDecimal progress = currentAmount.getAmount()
        .divide(targetAmount.getAmount(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"))
        .setScale(2, RoundingMode.HALF_UP);

    return progress;
  }

  /**
   * Calculates the remaining amount to reach the goal.
   *
   * @param currentAmount the current amount saved
   * @return the remaining amount (zero if goal is reached or exceeded)
   * @throws IllegalArgumentException if currentAmount is null or has different currency
   */
  public Money calculateRemaining(Money currentAmount) {
    validateCurrentAmount(currentAmount);

    BigDecimal remaining = targetAmount.getAmount().subtract(currentAmount.getAmount());

    if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
      return Money.zero(targetAmount.getCurrency());
    }

    return new Money(remaining, targetAmount.getCurrency());
  }

  /**
   * Checks if the goal has been reached.
   *
   * @param currentAmount the current amount saved
   * @return true if current amount is greater than or equal to target amount
   */
  public boolean isReached(Money currentAmount) {
    validateCurrentAmount(currentAmount);
    return currentAmount.getAmount().compareTo(targetAmount.getAmount()) >= 0;
  }

  /**
   * Checks if the deadline has passed.
   *
   * @return true if the deadline is in the past
   */
  public boolean isDeadlinePassed() {
    return LocalDate.now().isAfter(deadline);
  }

  /**
   * Updates the goal name.
   *
   * @param name the new name
   * @throws IllegalArgumentException if name is invalid
   */
  public void updateName(String name) {
    validateName(name);
    this.name = name.trim();
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * Updates the target amount.
   *
   * @param targetAmount the new target amount
   * @throws IllegalArgumentException if targetAmount is null
   */
  public void updateTargetAmount(Money targetAmount) {
    validateTargetAmount(targetAmount);
    this.targetAmount = targetAmount;
    this.updatedAt = LocalDateTime.now();
  }

  /**
   * Updates the deadline.
   *
   * @param deadline the new deadline
   * @throws IllegalArgumentException if deadline is invalid
   */
  public void updateDeadline(LocalDate deadline) {
    validateDeadline(deadline);
    this.deadline = deadline;
    this.updatedAt = LocalDateTime.now();
  }

  // Validation methods

  private static void validateUserId(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("User ID cannot be null");
    }
  }

  private static void validateName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Name cannot be null");
    }
    if (name.trim().isEmpty()) {
      throw new IllegalArgumentException("Name cannot be empty");
    }
  }

  private static void validateTargetAmount(Money targetAmount) {
    if (targetAmount == null) {
      throw new IllegalArgumentException("Target amount cannot be null");
    }
  }

  private static void validateDeadline(LocalDate deadline) {
    if (deadline == null) {
      throw new IllegalArgumentException("Deadline cannot be null");
    }
    if (deadline.isBefore(LocalDate.now())) {
      throw new IllegalArgumentException("Deadline cannot be in the past");
    }
  }

  private void validateCurrentAmount(Money currentAmount) {
    if (currentAmount == null) {
      throw new IllegalArgumentException("Current amount cannot be null");
    }
    if (!currentAmount.getCurrency().equals(targetAmount.getCurrency())) {
      throw new IllegalArgumentException(
          "Current amount must have the same currency as target amount");
    }
  }

  // Getters

  public UUID getId() {
    return id;
  }

  public UUID getUserId() {
    return userId;
  }

  public String getName() {
    return name;
  }

  public Money getTargetAmount() {
    return targetAmount;
  }

  public LocalDate getDeadline() {
    return deadline;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FinancialGoal that = (FinancialGoal) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "FinancialGoal{" + "id=" + id + ", userId=" + userId + ", name='" + name + '\''
        + ", targetAmount=" + targetAmount + ", deadline=" + deadline + ", createdAt=" + createdAt
        + ", updatedAt=" + updatedAt + '}';
  }
}
