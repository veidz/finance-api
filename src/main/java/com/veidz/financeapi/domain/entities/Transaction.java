package com.veidz.financeapi.domain.entities;

import com.veidz.financeapi.domain.enums.TransactionType;
import com.veidz.financeapi.domain.valueobjects.Money;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Transaction domain entity representing a financial transaction in the system. Contains business
 * logic and invariants for transaction management.
 *
 * @author Veidz
 */
public final class Transaction {

  private final UUID id;
  private final UUID userId;
  private final Money amount;
  private final TransactionType type;
  private String description;
  private UUID categoryId;
  private final LocalDateTime transactionDate;
  private final LocalDateTime createdAt;

  /**
   * Private constructor to prevent direct instantiation. Use static factory method instead.
   */
  private Transaction(UUID id, UUID userId, Money amount, TransactionType type, String description,
      LocalDateTime transactionDate, LocalDateTime createdAt) {
    this.id = id;
    this.userId = userId;
    this.amount = amount;
    this.type = type;
    this.description = description;
    this.transactionDate = transactionDate;
    this.createdAt = createdAt;
  }

  /**
   * Creates a new Transaction with the given details.
   *
   * @param userId the user ID who owns this transaction (must be non-null)
   * @param amount the transaction amount (must be non-null and positive)
   * @param type the transaction type (must be non-null)
   * @param description the transaction description (must be non-null and non-empty)
   * @param transactionDate the date and time of the transaction (must be non-null)
   * @return a new Transaction instance
   * @throws IllegalArgumentException if any validation fails
   */
  public static Transaction create(UUID userId, Money amount, TransactionType type,
      String description, LocalDateTime transactionDate) {
    validateUserId(userId);
    validateAmount(amount);
    validateType(type);
    validateDescription(description);
    validateTransactionDate(transactionDate);

    return new Transaction(UUID.randomUUID(), userId, amount, type, description.trim(),
        transactionDate, LocalDateTime.now());
  }

  /**
   * Gets the absolute amount of the balance impact. For direction (positive/negative), use
   * {@link #getType()}.
   *
   * @return the amount that impacts the balance
   */
  public Money getBalanceImpact() {
    return amount;
  }

  /**
   * Checks if this transaction increases the balance.
   *
   * @return true if income transaction, false if expense
   */
  public boolean isBalanceIncrease() {
    return type == TransactionType.INCOME;
  }

  /**
   * Assigns a category to this transaction.
   *
   * @param categoryId the category ID (can be null for uncategorized transactions)
   */
  public void assignCategory(UUID categoryId) {
    this.categoryId = categoryId;
  }

  /**
   * Updates the transaction description.
   *
   * @param newDescription the new description (must be non-null and non-empty)
   * @throws IllegalArgumentException if description is invalid
   */
  public void updateDescription(String newDescription) {
    validateDescription(newDescription);
    this.description = newDescription.trim();
  }

  private static void validateUserId(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("User ID cannot be null");
    }
  }

  private static void validateAmount(Money amount) {
    if (amount == null) {
      throw new IllegalArgumentException("Amount cannot be null");
    }
    if (amount.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Amount must be greater than zero");
    }
  }

  private static void validateType(TransactionType type) {
    if (type == null) {
      throw new IllegalArgumentException("Transaction type cannot be null");
    }
  }

  private static void validateDescription(String description) {
    if (description == null) {
      throw new IllegalArgumentException("Description cannot be null");
    }
    if (description.trim().isEmpty()) {
      throw new IllegalArgumentException("Description cannot be empty");
    }
  }

  private static void validateTransactionDate(LocalDateTime transactionDate) {
    if (transactionDate == null) {
      throw new IllegalArgumentException("Transaction date cannot be null");
    }
  }

  public UUID getId() {
    return id;
  }

  public UUID getUserId() {
    return userId;
  }

  public Money getAmount() {
    return amount;
  }

  public TransactionType getType() {
    return type;
  }

  public String getDescription() {
    return description;
  }

  public UUID getCategoryId() {
    return categoryId;
  }

  public LocalDateTime getTransactionDate() {
    return transactionDate;
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
    Transaction that = (Transaction) obj;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Transaction{" + "id=" + id + ", userId=" + userId + ", amount=" + amount + ", type="
        + type + ", description='" + description + '\'' + ", categoryId=" + categoryId
        + ", transactionDate=" + transactionDate + ", createdAt=" + createdAt + '}';
  }
}
