package com.veidz.financeapi.domain.entities;

import com.veidz.financeapi.domain.valueobjects.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("FinancialGoal Entity Tests")
class FinancialGoalTest {

  // Test Data Builder
  private static class TestDataBuilder {
    private UUID userId = UUID.randomUUID();
    private String name = "Test Goal";
    private Money targetAmount = money("1000.00");
    private LocalDate deadline = LocalDate.now().plusMonths(6);

    TestDataBuilder withUserId(UUID userId) {
      this.userId = userId;
      return this;
    }

    TestDataBuilder withName(String name) {
      this.name = name;
      return this;
    }

    TestDataBuilder withTargetAmount(String amount) {
      this.targetAmount = money(amount);
      return this;
    }

    TestDataBuilder withDeadline(LocalDate deadline) {
      this.deadline = deadline;
      return this;
    }

    FinancialGoal build() {
      return FinancialGoal.create(userId, name, targetAmount, deadline);
    }
  }

  private static TestDataBuilder aGoal() {
    return new TestDataBuilder();
  }

  private static Money money(String amount) {
    return new Money(new BigDecimal(amount), Currency.getInstance("BRL"));
  }

  @Nested @DisplayName("Creation Tests")
  class CreationTests {

    @Test @DisplayName("Should create goal with valid data")
    void shouldCreateWithValidData() {
      UUID userId = UUID.randomUUID();
      String name = "Emergency Fund";
      Money targetAmount = money("10000.00");
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

    @Test @DisplayName("Should throw exception when user ID is null")
    void shouldRejectNullUserId() {
      assertThrows(IllegalArgumentException.class, () -> aGoal().withUserId(null).build());
    }

    @Test @DisplayName("Should throw exception when name is null")
    void shouldRejectNullName() {
      assertThrows(IllegalArgumentException.class, () -> aGoal().withName(null).build());
    }

    @Test @DisplayName("Should throw exception when name is empty")
    void shouldRejectEmptyName() {
      assertThrows(IllegalArgumentException.class, () -> aGoal().withName("").build());
    }

    @Test @DisplayName("Should throw exception when name is blank")
    void shouldRejectBlankName() {
      assertThrows(IllegalArgumentException.class, () -> aGoal().withName("   ").build());
    }

    @Test @DisplayName("Should throw exception when target amount is null")
    void shouldRejectNullTargetAmount() {
      assertThrows(IllegalArgumentException.class, () -> FinancialGoal.create(UUID.randomUUID(),
          "Test", null, LocalDate.now().plusMonths(1)));
    }

    @Test @DisplayName("Should throw exception when deadline is null")
    void shouldRejectNullDeadline() {
      assertThrows(IllegalArgumentException.class, () -> aGoal().withDeadline(null).build());
    }

    @Test @DisplayName("Should throw exception when deadline is in the past")
    void shouldRejectPastDeadline() {
      LocalDate pastDate = LocalDate.now().minusDays(1);
      assertThrows(IllegalArgumentException.class, () -> aGoal().withDeadline(pastDate).build());
    }

    @Test @DisplayName("Should allow deadline to be today")
    void shouldAllowTodayAsDeadline() {
      LocalDate today = LocalDate.now();
      FinancialGoal goal = aGoal().withDeadline(today).build();

      assertEquals(today, goal.getDeadline());
    }
  }

  @Nested @DisplayName("Progress Calculation Tests")
  class ProgressCalculationTests {

    @Test @DisplayName("Should calculate progress percentage correctly")
    void shouldCalculateProgressPercentage() {
      FinancialGoal goal = aGoal().withTargetAmount("1000.00").build();
      Money currentAmount = money("250.00");

      BigDecimal progress = goal.calculateProgress(currentAmount);

      assertEquals(new BigDecimal("25.00"), progress);
    }

    @Test @DisplayName("Should return zero progress when current amount is zero")
    void shouldReturnZeroProgressForZeroAmount() {
      FinancialGoal goal = aGoal().build();
      Money currentAmount = money("0.00");

      BigDecimal progress = goal.calculateProgress(currentAmount);

      assertEquals(BigDecimal.ZERO, progress);
    }

    @Test @DisplayName("Should return 100% when goal is reached")
    void shouldReturnFullProgressWhenReached() {
      FinancialGoal goal = aGoal().withTargetAmount("1000.00").build();
      Money currentAmount = money("1000.00");

      BigDecimal progress = goal.calculateProgress(currentAmount);

      assertEquals(new BigDecimal("100.00"), progress);
    }

    @Test @DisplayName("Should allow progress over 100%")
    void shouldAllowProgressOverOneHundred() {
      FinancialGoal goal = aGoal().withTargetAmount("1000.00").build();
      Money currentAmount = money("1500.00");

      BigDecimal progress = goal.calculateProgress(currentAmount);

      assertEquals(new BigDecimal("150.00"), progress);
    }

    @Test @DisplayName("Should throw exception when calculating progress with null amount")
    void shouldRejectNullAmountInProgress() {
      FinancialGoal goal = aGoal().build();

      assertThrows(IllegalArgumentException.class, () -> goal.calculateProgress(null));
    }

    @Test @DisplayName("Should throw exception when calculating progress with different currency")
    void shouldRejectDifferentCurrencyInProgress() {
      FinancialGoal goal = aGoal().build();
      Money usdAmount = new Money(new BigDecimal("200.00"), Currency.getInstance("USD"));

      assertThrows(IllegalArgumentException.class, () -> goal.calculateProgress(usdAmount));
    }
  }

  @Nested @DisplayName("Remaining Amount Calculation Tests")
  class RemainingAmountTests {

    @Test @DisplayName("Should calculate remaining amount correctly")
    void shouldCalculateRemainingAmount() {
      FinancialGoal goal = aGoal().withTargetAmount("10000.00").build();
      Money currentAmount = money("3000.00");

      Money remaining = goal.calculateRemaining(currentAmount);

      assertEquals(money("7000.00"), remaining);
    }

    @Test @DisplayName("Should return zero when goal is reached")
    void shouldReturnZeroWhenReached() {
      FinancialGoal goal = aGoal().withTargetAmount("1000.00").build();
      Money currentAmount = money("1000.00");

      Money remaining = goal.calculateRemaining(currentAmount);

      assertEquals(money("0.00"), remaining);
    }

    @Test @DisplayName("Should return zero when goal is exceeded")
    void shouldReturnZeroWhenExceeded() {
      FinancialGoal goal = aGoal().withTargetAmount("1000.00").build();
      Money currentAmount = money("1500.00");

      Money remaining = goal.calculateRemaining(currentAmount);

      assertEquals(money("0.00"), remaining);
    }

    @Test @DisplayName("Should throw exception when calculating remaining with null amount")
    void shouldRejectNullAmountInRemaining() {
      FinancialGoal goal = aGoal().build();

      assertThrows(IllegalArgumentException.class, () -> goal.calculateRemaining(null));
    }

    @Test @DisplayName("Should throw exception when calculating remaining with different currency")
    void shouldRejectDifferentCurrencyInRemaining() {
      FinancialGoal goal = aGoal().build();
      Money eurAmount = new Money(new BigDecimal("200.00"), Currency.getInstance("EUR"));

      assertThrows(IllegalArgumentException.class, () -> goal.calculateRemaining(eurAmount));
    }
  }

  @Nested @DisplayName("Goal Status Tests")
  class GoalStatusTests {

    @Test @DisplayName("Should recognize when goal is reached")
    void shouldRecognizeGoalReached() {
      FinancialGoal goal = aGoal().withTargetAmount("1000.00").build();
      Money currentAmount = money("1000.00");

      assertTrue(goal.isReached(currentAmount));
    }

    @Test @DisplayName("Should recognize when goal is not reached")
    void shouldRecognizeGoalNotReached() {
      FinancialGoal goal = aGoal().withTargetAmount("1000.00").build();
      Money currentAmount = money("500.00");

      assertFalse(goal.isReached(currentAmount));
    }

    @Test @DisplayName("Should consider goal reached when exceeded")
    void shouldConsiderExceededAsReached() {
      FinancialGoal goal = aGoal().withTargetAmount("1000.00").build();
      Money currentAmount = money("1500.00");

      assertTrue(goal.isReached(currentAmount));
    }

    @Test @DisplayName("Should check if deadline has not passed for future deadline")
    void shouldCheckFutureDeadlineNotPassed() {
      LocalDate futureDeadline = LocalDate.now().plusDays(1);
      FinancialGoal goal = aGoal().withDeadline(futureDeadline).build();

      assertFalse(goal.isDeadlinePassed());
    }

    @Test @DisplayName("Should check if deadline is today")
    void shouldCheckTodayDeadlineNotPassed() {
      LocalDate today = LocalDate.now();
      FinancialGoal goal = aGoal().withDeadline(today).build();

      assertFalse(goal.isDeadlinePassed());
    }
  }

  @Nested @DisplayName("Update Tests")
  class UpdateTests {

    @Test @DisplayName("Should update name successfully")
    void shouldUpdateName() {
      FinancialGoal goal = aGoal().withName("Original Goal").build();
      String newName = "Updated Goal Name";

      goal.updateName(newName);

      assertEquals(newName, goal.getName());
    }

    @Test @DisplayName("Should throw exception when updating to null name")
    void shouldRejectNullInNameUpdate() {
      FinancialGoal goal = aGoal().build();

      assertThrows(IllegalArgumentException.class, () -> goal.updateName(null));
    }

    @Test @DisplayName("Should throw exception when updating to empty name")
    void shouldRejectEmptyInNameUpdate() {
      FinancialGoal goal = aGoal().build();

      assertThrows(IllegalArgumentException.class, () -> goal.updateName(""));
    }

    @Test @DisplayName("Should update target amount successfully")
    void shouldUpdateTargetAmount() {
      FinancialGoal goal = aGoal().withTargetAmount("1000.00").build();
      Money newTarget = money("2000.00");

      goal.updateTargetAmount(newTarget);

      assertEquals(newTarget, goal.getTargetAmount());
    }

    @Test @DisplayName("Should throw exception when updating to null target amount")
    void shouldRejectNullInTargetUpdate() {
      FinancialGoal goal = aGoal().build();

      assertThrows(IllegalArgumentException.class, () -> goal.updateTargetAmount(null));
    }

    @Test @DisplayName("Should update deadline successfully")
    void shouldUpdateDeadline() {
      LocalDate originalDeadline = LocalDate.now().plusMonths(6);
      FinancialGoal goal = aGoal().withDeadline(originalDeadline).build();
      LocalDate newDeadline = LocalDate.now().plusMonths(12);

      goal.updateDeadline(newDeadline);

      assertEquals(newDeadline, goal.getDeadline());
    }

    @Test @DisplayName("Should throw exception when updating to null deadline")
    void shouldRejectNullInDeadlineUpdate() {
      FinancialGoal goal = aGoal().build();

      assertThrows(IllegalArgumentException.class, () -> goal.updateDeadline(null));
    }

    @Test @DisplayName("Should throw exception when updating to past deadline")
    void shouldRejectPastInDeadlineUpdate() {
      FinancialGoal goal = aGoal().build();
      LocalDate pastDate = LocalDate.now().minusDays(1);

      assertThrows(IllegalArgumentException.class, () -> goal.updateDeadline(pastDate));
    }
  }
}
