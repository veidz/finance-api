package com.veidz.financeapi.domain.entities;

import static org.junit.jupiter.api.Assertions.*;

import com.veidz.financeapi.domain.enums.TransactionType;
import com.veidz.financeapi.domain.valueobjects.Money;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Transaction Entity Tests")
class TransactionTest {

  // Test Data Builder
  private static class TestDataBuilder {
    private UUID userId = UUID.randomUUID();
    private Money amount = money("100.00");
    private TransactionType type = TransactionType.EXPENSE;
    private String description = "Test Transaction";
    private LocalDateTime transactionDate = LocalDateTime.now();

    TestDataBuilder withUserId(UUID userId) {
      this.userId = userId;
      return this;
    }

    TestDataBuilder withAmount(String amount) {
      this.amount = money(amount);
      return this;
    }

    TestDataBuilder withType(TransactionType type) {
      this.type = type;
      return this;
    }

    TestDataBuilder withDescription(String description) {
      this.description = description;
      return this;
    }

    TestDataBuilder withTransactionDate(LocalDateTime transactionDate) {
      this.transactionDate = transactionDate;
      return this;
    }

    Transaction build() {
      return Transaction.create(userId, amount, type, description, transactionDate);
    }
  }

  private static TestDataBuilder aTransaction() {
    return new TestDataBuilder();
  }

  private static Money money(String amount) {
    return new Money(new BigDecimal(amount), Currency.getInstance("USD"));
  }

  @Nested
  @DisplayName("Creation Tests")
  class CreationTests {

    @Test
    @DisplayName("Should create transaction with all required fields")
    void shouldCreateWithAllFields() {
      UUID userId = UUID.randomUUID();
      Money amount = money("100.00");
      String description = "Salary payment";
      LocalDateTime transactionDate = LocalDateTime.now();

      Transaction transaction =
          Transaction.create(userId, amount, TransactionType.INCOME, description, transactionDate);

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
    @DisplayName("Should create expense transaction")
    void shouldCreateExpenseTransaction() {
      Transaction transaction = aTransaction().withType(TransactionType.EXPENSE).build();

      assertEquals(TransactionType.EXPENSE, transaction.getType());
    }

    @Test
    @DisplayName("Should throw exception when user ID is null")
    void shouldRejectNullUserId() {
      assertThrows(
          IllegalArgumentException.class, () -> aTransaction().withUserId(null).build());
    }

    @Test
    @DisplayName("Should throw exception when amount is null")
    void shouldRejectNullAmount() {
      assertThrows(
          IllegalArgumentException.class,
          () ->
              Transaction.create(
                  UUID.randomUUID(),
                  null,
                  TransactionType.INCOME,
                  "Test",
                  LocalDateTime.now()));
    }

    @Test
    @DisplayName("Should throw exception when type is null")
    void shouldRejectNullType() {
      assertThrows(
          IllegalArgumentException.class, () -> aTransaction().withType(null).build());
    }

    @Test
    @DisplayName("Should throw exception when description is null")
    void shouldRejectNullDescription() {
      assertThrows(
          IllegalArgumentException.class, () -> aTransaction().withDescription(null).build());
    }

    @Test
    @DisplayName("Should throw exception when description is empty")
    void shouldRejectEmptyDescription() {
      assertThrows(
          IllegalArgumentException.class, () -> aTransaction().withDescription("").build());
    }

    @Test
    @DisplayName("Should throw exception when description is blank")
    void shouldRejectBlankDescription() {
      assertThrows(
          IllegalArgumentException.class, () -> aTransaction().withDescription("   ").build());
    }

    @Test
    @DisplayName("Should throw exception when transaction date is null")
    void shouldRejectNullTransactionDate() {
      assertThrows(
          IllegalArgumentException.class,
          () -> aTransaction().withTransactionDate(null).build());
    }
  }

  @Nested
  @DisplayName("Balance Impact Tests")
  class BalanceImpactTests {

    @Test
    @DisplayName("Should calculate balance impact for income")
    void shouldCalculateBalanceImpactForIncome() {
      Transaction transaction =
          aTransaction().withType(TransactionType.INCOME).withAmount("100.00").build();

      Money balanceImpact = transaction.getBalanceImpact();

      assertEquals(money("100.00"), balanceImpact);
      assertEquals(0, balanceImpact.getAmount().compareTo(BigDecimal.valueOf(100.00)));
      assertTrue(transaction.isBalanceIncrease());
    }

    @Test
    @DisplayName("Should calculate balance impact for expense")
    void shouldCalculateBalanceImpactForExpense() {
      Transaction transaction =
          aTransaction().withType(TransactionType.EXPENSE).withAmount("100.00").build();

      Money balanceImpact = transaction.getBalanceImpact();

      assertEquals(money("100.00"), balanceImpact);
      assertEquals(0, balanceImpact.getAmount().compareTo(BigDecimal.valueOf(100.00)));
      assertFalse(transaction.isBalanceIncrease());
    }
  }

  @Nested
  @DisplayName("Category Assignment Tests")
  class CategoryAssignmentTests {

    @Test
    @DisplayName("Should allow category assignment")
    void shouldAllowCategoryAssignment() {
      Transaction transaction = aTransaction().build();
      UUID categoryId = UUID.randomUUID();

      transaction.assignCategory(categoryId);

      assertEquals(categoryId, transaction.getCategoryId());
    }

    @Test
    @DisplayName("Should allow null category for uncategorized transactions")
    void shouldAllowNullCategory() {
      Transaction transaction = aTransaction().build();

      assertDoesNotThrow(() -> transaction.assignCategory(null));
    }
  }

  @Nested
  @DisplayName("Update Tests")
  class UpdateTests {

    @Test
    @DisplayName("Should update description successfully")
    void shouldUpdateDescription() {
      Transaction transaction = aTransaction().withDescription("Old description").build();

      transaction.updateDescription("New description");

      assertEquals("New description", transaction.getDescription());
    }

    @Test
    @DisplayName("Should throw exception when updating to null description")
    void shouldRejectNullInDescriptionUpdate() {
      Transaction transaction = aTransaction().build();

      assertThrows(IllegalArgumentException.class, () -> transaction.updateDescription(null));
    }

    @Test
    @DisplayName("Should throw exception when updating to empty description")
    void shouldRejectEmptyInDescriptionUpdate() {
      Transaction transaction = aTransaction().build();

      assertThrows(IllegalArgumentException.class, () -> transaction.updateDescription(""));
    }
  }
}
