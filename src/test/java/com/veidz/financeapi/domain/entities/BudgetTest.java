package com.veidz.financeapi.domain.entities;

import static org.junit.jupiter.api.Assertions.*;

import com.veidz.financeapi.domain.valueobjects.DateRange;
import com.veidz.financeapi.domain.valueobjects.Money;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Budget Entity Tests")
class BudgetTest {

  // Test Data Builder
  private static class TestDataBuilder {
    private UUID userId = UUID.randomUUID();
    private String name = "Test Budget";
    private Money amount = money("1000.00");
    private DateRange period = new DateRange(LocalDate.now().withDayOfMonth(1),
        LocalDate.now().withDayOfMonth(1).plusMonths(1).minusDays(1));
    private UUID categoryId = null;

    TestDataBuilder withUserId(UUID userId) {
      this.userId = userId;
      return this;
    }

    TestDataBuilder withName(String name) {
      this.name = name;
      return this;
    }

    TestDataBuilder withAmount(String amount) {
      this.amount = money(amount);
      return this;
    }

    TestDataBuilder withPeriod(LocalDate start, LocalDate end) {
      this.period = new DateRange(start, end);
      return this;
    }

    TestDataBuilder withCategoryId(UUID categoryId) {
      this.categoryId = categoryId;
      return this;
    }

    Budget build() {
      return categoryId == null ? Budget.create(userId, name, amount, period)
          : Budget.create(userId, name, amount, period, categoryId);
    }
  }

  private static TestDataBuilder aBudget() {
    return new TestDataBuilder();
  }

  private static Money money(String amount) {
    return new Money(new BigDecimal(amount), Currency.getInstance("USD"));
  }

  @Nested @DisplayName("Creation Tests")
  class CreationTests {

    @Test @DisplayName("Should create budget with required fields")
    void shouldCreateWithRequiredFields() {
      UUID userId = UUID.randomUUID();
      String name = "Monthly Groceries";
      Money amount = money("500.00");
      DateRange period = new DateRange(LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 30));

      Budget budget = Budget.create(userId, name, amount, period);

      assertNotNull(budget);
      assertNotNull(budget.getId());
      assertEquals(userId, budget.getUserId());
      assertEquals(name, budget.getName());
      assertEquals(amount, budget.getAmount());
      assertEquals(period, budget.getPeriod());
      assertNull(budget.getCategoryId());
      assertNotNull(budget.getCreatedAt());
    }

    @Test @DisplayName("Should create budget with category")
    void shouldCreateWithCategory() {
      UUID categoryId = UUID.randomUUID();
      Budget budget = aBudget().withCategoryId(categoryId).build();

      assertEquals(categoryId, budget.getCategoryId());
    }

    @Test @DisplayName("Should throw exception when user ID is null")
    void shouldRejectNullUserId() {
      assertThrows(IllegalArgumentException.class, () -> aBudget().withUserId(null).build());
    }

    @Test @DisplayName("Should throw exception when name is null")
    void shouldRejectNullName() {
      assertThrows(IllegalArgumentException.class, () -> aBudget().withName(null).build());
    }

    @Test @DisplayName("Should throw exception when name is empty")
    void shouldRejectEmptyName() {
      assertThrows(IllegalArgumentException.class, () -> aBudget().withName("").build());
    }

    @Test @DisplayName("Should throw exception when name is blank")
    void shouldRejectBlankName() {
      assertThrows(IllegalArgumentException.class, () -> aBudget().withName("   ").build());
    }

    @Test @DisplayName("Should throw exception when amount is null")
    void shouldRejectNullAmount() {
      assertThrows(IllegalArgumentException.class, () -> Budget.create(UUID.randomUUID(), "Test",
          null, new DateRange(LocalDate.now(), LocalDate.now().plusDays(1))));
    }

    @Test @DisplayName("Should throw exception when period is null")
    void shouldRejectNullPeriod() {
      assertThrows(IllegalArgumentException.class,
          () -> Budget.create(UUID.randomUUID(), "Test", money("100.00"), null));
    }
  }

  @Nested @DisplayName("Spending Calculation Tests")
  class SpendingCalculationTests {

    @Test @DisplayName("Should calculate remaining amount correctly")
    void shouldCalculateRemaining() {
      Budget budget = aBudget().withAmount("1000.00").build();
      Money spent = money("300.00");

      BigDecimal remaining = budget.calculateRemaining(spent);

      assertEquals(new BigDecimal("700.00"), remaining);
    }

    @Test @DisplayName("Should return zero when budget equals spending")
    void shouldReturnZeroWhenEqual() {
      Budget budget = aBudget().withAmount("1000.00").build();
      Money spent = money("1000.00");

      BigDecimal remaining = budget.calculateRemaining(spent);

      assertEquals(new BigDecimal("0.00"), remaining);
    }

    @Test @DisplayName("Should return negative when overspent")
    void shouldReturnNegativeWhenOverspent() {
      Budget budget = aBudget().withAmount("1000.00").build();
      Money spent = money("1500.00");

      BigDecimal remaining = budget.calculateRemaining(spent);

      assertEquals(new BigDecimal("-500.00"), remaining);
    }

    @Test @DisplayName("Should throw exception when calculating with null spent amount")
    void shouldRejectNullInRemaining() {
      Budget budget = aBudget().build();

      assertThrows(IllegalArgumentException.class, () -> budget.calculateRemaining(null));
    }

    @Test @DisplayName("Should throw exception when calculating with different currency")
    void shouldRejectDifferentCurrencyInRemaining() {
      Budget budget = aBudget().build();
      Money eurSpent = new Money(new BigDecimal("100.00"), Currency.getInstance("EUR"));

      assertThrows(IllegalArgumentException.class, () -> budget.calculateRemaining(eurSpent));
    }

    @Test @DisplayName("Should calculate percentage used correctly")
    void shouldCalculatePercentageUsed() {
      Budget budget = aBudget().withAmount("1000.00").build();
      Money spent = money("250.00");

      BigDecimal percentage = budget.calculatePercentageUsed(spent);

      assertEquals(new BigDecimal("25.00"), percentage);
    }

    @Test @DisplayName("Should return 100% when fully used")
    void shouldReturnFullPercentage() {
      Budget budget = aBudget().withAmount("1000.00").build();
      Money spent = money("1000.00");

      BigDecimal percentage = budget.calculatePercentageUsed(spent);

      assertEquals(new BigDecimal("100.00"), percentage);
    }

    @Test @DisplayName("Should allow percentage over 100%")
    void shouldAllowPercentageOverOneHundred() {
      Budget budget = aBudget().withAmount("1000.00").build();
      Money spent = money("1500.00");

      BigDecimal percentage = budget.calculatePercentageUsed(spent);

      assertEquals(new BigDecimal("150.00"), percentage);
    }

    @Test @DisplayName("Should return zero percentage for zero spending")
    void shouldReturnZeroPercentageForZero() {
      Budget budget = aBudget().build();
      Money spent = money("0.00");

      BigDecimal percentage = budget.calculatePercentageUsed(spent);

      assertEquals(new BigDecimal("0.00"), percentage);
    }

    @Test @DisplayName("Should throw exception when calculating percentage with null")
    void shouldRejectNullInPercentage() {
      Budget budget = aBudget().build();

      assertThrows(IllegalArgumentException.class, () -> budget.calculatePercentageUsed(null));
    }

    @Test @DisplayName("Should throw exception when calculating percentage with different currency")
    void shouldRejectDifferentCurrencyInPercentage() {
      Budget budget = aBudget().build();
      Money eurSpent = new Money(new BigDecimal("100.00"), Currency.getInstance("EUR"));

      assertThrows(IllegalArgumentException.class, () -> budget.calculatePercentageUsed(eurSpent));
    }
  }

  @Nested @DisplayName("Budget Status Tests")
  class BudgetStatusTests {

    @Test @DisplayName("Should recognize when budget is exceeded")
    void shouldRecognizeExceeded() {
      Budget budget = aBudget().withAmount("1000.00").build();
      Money spent = money("1500.00");

      assertTrue(budget.isExceeded(spent));
    }

    @Test @DisplayName("Should recognize when budget is not exceeded")
    void shouldRecognizeNotExceeded() {
      Budget budget = aBudget().withAmount("1000.00").build();
      Money spent = money("500.00");

      assertFalse(budget.isExceeded(spent));
    }

    @Test @DisplayName("Should not be exceeded when equal to budget")
    void shouldNotBeExceededWhenEqual() {
      Budget budget = aBudget().withAmount("1000.00").build();
      Money spent = money("1000.00");

      assertFalse(budget.isExceeded(spent));
    }

    @Test @DisplayName("Should check if budget is active for current date")
    void shouldCheckActive() {
      LocalDate today = LocalDate.now();
      Budget budget = aBudget().withPeriod(today.minusDays(1), today.plusDays(1)).build();

      assertTrue(budget.isActive());
    }

    @Test @DisplayName("Should check if budget is not active for past period")
    void shouldCheckNotActive() {
      LocalDate pastStart = LocalDate.now().minusMonths(2);
      LocalDate pastEnd = LocalDate.now().minusMonths(1);
      Budget budget = aBudget().withPeriod(pastStart, pastEnd).build();

      assertFalse(budget.isActive());
    }
  }

  @Nested @DisplayName("Update Tests")
  class UpdateTests {

    @Test @DisplayName("Should update name successfully")
    void shouldUpdateName() {
      Budget budget = aBudget().withName("Original Name").build();
      String newName = "Updated Name";

      budget.updateName(newName);

      assertEquals(newName, budget.getName());
    }

    @Test @DisplayName("Should throw exception when updating to null name")
    void shouldRejectNullInNameUpdate() {
      Budget budget = aBudget().build();

      assertThrows(IllegalArgumentException.class, () -> budget.updateName(null));
    }

    @Test @DisplayName("Should throw exception when updating to empty name")
    void shouldRejectEmptyInNameUpdate() {
      Budget budget = aBudget().build();

      assertThrows(IllegalArgumentException.class, () -> budget.updateName(""));
    }

    @Test @DisplayName("Should update amount successfully")
    void shouldUpdateAmount() {
      Budget budget = aBudget().withAmount("1000.00").build();
      Money newAmount = money("2000.00");

      budget.updateAmount(newAmount);

      assertEquals(newAmount, budget.getAmount());
    }

    @Test @DisplayName("Should throw exception when updating to null amount")
    void shouldRejectNullInAmountUpdate() {
      Budget budget = aBudget().build();

      assertThrows(IllegalArgumentException.class, () -> budget.updateAmount(null));
    }

    @Test @DisplayName("Should set category ID successfully")
    void shouldSetCategoryId() {
      Budget budget = aBudget().build();
      UUID categoryId = UUID.randomUUID();

      budget.setCategoryId(categoryId);

      assertEquals(categoryId, budget.getCategoryId());
    }

    @Test @DisplayName("Should clear category ID with null")
    void shouldClearCategoryId() {
      UUID categoryId = UUID.randomUUID();
      Budget budget = aBudget().withCategoryId(categoryId).build();

      budget.setCategoryId(null);

      assertNull(budget.getCategoryId());
    }
  }
}
