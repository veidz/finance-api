package com.veidz.financeapi.domain.entities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.veidz.financeapi.domain.enums.TransactionType;
import com.veidz.financeapi.domain.valueobjects.Money;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Transaction entity.
 *
 * @author Veidz
 */
class TransactionTest {

  @Test
  void shouldCreateTransactionWithAllRequiredFields() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(100.00), Currency.getInstance("USD"));
    String description = "Salary payment";
    LocalDateTime transactionDate = LocalDateTime.now();

    // When
    Transaction transaction =
        Transaction.create(userId, amount, TransactionType.INCOME, description, transactionDate);

    // Then
    assertNotNull(transaction);
    assertNotNull(transaction.getId());
    assertEquals(userId, transaction.getUserId());
    assertEquals(amount, transaction.getAmount());
    assertEquals(TransactionType.INCOME, transaction.getType());
    assertEquals(description, transaction.getDescription());
    assertEquals(transactionDate, transaction.getTransactionDate());
    assertNotNull(transaction.getCreatedAt());
  }

  @Test
  void shouldCreateExpenseTransaction() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(50.00), Currency.getInstance("USD"));
    String description = "Grocery shopping";
    LocalDateTime transactionDate = LocalDateTime.now();

    // When
    Transaction transaction =
        Transaction.create(userId, amount, TransactionType.EXPENSE, description, transactionDate);

    // Then
    assertEquals(TransactionType.EXPENSE, transaction.getType());
  }

  @Test
  void shouldThrowExceptionWhenUserIdIsNull() {
    // Given
    Money amount = new Money(BigDecimal.valueOf(100.00), Currency.getInstance("USD"));
    String description = "Test transaction";
    LocalDateTime transactionDate = LocalDateTime.now();

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> Transaction.create(null, amount, TransactionType.INCOME, description, transactionDate));
  }

  @Test
  void shouldThrowExceptionWhenAmountIsNull() {
    // Given
    UUID userId = UUID.randomUUID();
    String description = "Test transaction";
    LocalDateTime transactionDate = LocalDateTime.now();

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> Transaction.create(userId, null, TransactionType.INCOME, description, transactionDate));
  }

  @Test
  void shouldThrowExceptionWhenAmountIsZero() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = Money.zero(Currency.getInstance("USD"));
    String description = "Test transaction";
    LocalDateTime transactionDate = LocalDateTime.now();

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> Transaction.create(userId, amount, TransactionType.INCOME, description, transactionDate));
  }

  @Test
  void shouldThrowExceptionWhenCreatingMoneyWithNegativeAmount() {
    // Given
    UUID userId = UUID.randomUUID();
    String description = "Test transaction";
    LocalDateTime transactionDate = LocalDateTime.now();

    // When & Then - Money value object should reject negative amounts
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          Money amount = new Money(BigDecimal.valueOf(-100.00), Currency.getInstance("USD"));
          Transaction.create(userId, amount, TransactionType.INCOME, description, transactionDate);
        });
  }

  @Test
  void shouldThrowExceptionWhenTypeIsNull() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(100.00), Currency.getInstance("USD"));
    String description = "Test transaction";
    LocalDateTime transactionDate = LocalDateTime.now();

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> Transaction.create(userId, amount, null, description, transactionDate));
  }

  @Test
  void shouldThrowExceptionWhenDescriptionIsNull() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(100.00), Currency.getInstance("USD"));
    LocalDateTime transactionDate = LocalDateTime.now();

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> Transaction.create(userId, amount, TransactionType.INCOME, null, transactionDate));
  }

  @Test
  void shouldThrowExceptionWhenDescriptionIsEmpty() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(100.00), Currency.getInstance("USD"));
    LocalDateTime transactionDate = LocalDateTime.now();

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> Transaction.create(userId, amount, TransactionType.INCOME, "", transactionDate));
  }

  @Test
  void shouldThrowExceptionWhenDescriptionIsBlank() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(100.00), Currency.getInstance("USD"));
    LocalDateTime transactionDate = LocalDateTime.now();

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> Transaction.create(userId, amount, TransactionType.INCOME, "   ", transactionDate));
  }

  @Test
  void shouldThrowExceptionWhenTransactionDateIsNull() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(100.00), Currency.getInstance("USD"));
    String description = "Test transaction";

    // When & Then
    assertThrows(
        IllegalArgumentException.class,
        () -> Transaction.create(userId, amount, TransactionType.INCOME, description, null));
  }

  @Test
  void shouldTrimDescriptionWhitespace() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(100.00), Currency.getInstance("USD"));
    String description = "  Test transaction  ";
    LocalDateTime transactionDate = LocalDateTime.now();

    // When
    Transaction transaction =
        Transaction.create(userId, amount, TransactionType.INCOME, description, transactionDate);

    // Then
    assertEquals("Test transaction", transaction.getDescription());
  }

  @Test
  void shouldGetBalanceImpactAmountForIncome() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(100.00), Currency.getInstance("USD"));
    LocalDateTime transactionDate = LocalDateTime.now();

    Transaction transaction =
        Transaction.create(userId, amount, TransactionType.INCOME, "Salary", transactionDate);

    // When
    Money balanceImpact = transaction.getBalanceImpact();

    // Then
    assertEquals(amount, balanceImpact);
    assertEquals(0, balanceImpact.getAmount().compareTo(BigDecimal.valueOf(100.00)));
    assertEquals(true, transaction.isBalanceIncrease());
  }

  @Test
  void shouldGetBalanceImpactAmountForExpense() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(100.00), Currency.getInstance("USD"));
    LocalDateTime transactionDate = LocalDateTime.now();

    Transaction transaction =
        Transaction.create(userId, amount, TransactionType.EXPENSE, "Groceries", transactionDate);

    // When
    Money balanceImpact = transaction.getBalanceImpact();

    // Then
    assertEquals(amount, balanceImpact);
    assertEquals(0, balanceImpact.getAmount().compareTo(BigDecimal.valueOf(100.00)));
    assertEquals(false, transaction.isBalanceIncrease());
  }

  @Test
  void shouldAllowCategoryAssignment() {
    // Given
    UUID userId = UUID.randomUUID();
    UUID categoryId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(100.00), Currency.getInstance("USD"));
    LocalDateTime transactionDate = LocalDateTime.now();

    Transaction transaction =
        Transaction.create(userId, amount, TransactionType.EXPENSE, "Shopping", transactionDate);

    // When
    transaction.assignCategory(categoryId);

    // Then
    assertEquals(categoryId, transaction.getCategoryId());
  }

  @Test
  void shouldAllowNullCategoryForUncategorizedTransactions() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(100.00), Currency.getInstance("USD"));
    LocalDateTime transactionDate = LocalDateTime.now();

    // When
    Transaction transaction =
        Transaction.create(userId, amount, TransactionType.EXPENSE, "Misc", transactionDate);

    // Then
    assertDoesNotThrow(() -> transaction.assignCategory(null));
  }

  @Test
  void shouldUpdateDescription() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(100.00), Currency.getInstance("USD"));
    LocalDateTime transactionDate = LocalDateTime.now();

    Transaction transaction =
        Transaction.create(userId, amount, TransactionType.EXPENSE, "Old description", transactionDate);

    // When
    transaction.updateDescription("New description");

    // Then
    assertEquals("New description", transaction.getDescription());
  }

  @Test
  void shouldThrowExceptionWhenUpdatingDescriptionToNull() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(100.00), Currency.getInstance("USD"));
    LocalDateTime transactionDate = LocalDateTime.now();

    Transaction transaction =
        Transaction.create(userId, amount, TransactionType.EXPENSE, "Old description", transactionDate);

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> transaction.updateDescription(null));
  }

  @Test
  void shouldThrowExceptionWhenUpdatingDescriptionToEmpty() {
    // Given
    UUID userId = UUID.randomUUID();
    Money amount = new Money(BigDecimal.valueOf(100.00), Currency.getInstance("USD"));
    LocalDateTime transactionDate = LocalDateTime.now();

    Transaction transaction =
        Transaction.create(userId, amount, TransactionType.EXPENSE, "Old description", transactionDate);

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> transaction.updateDescription(""));
  }
}
