package com.veidz.financeapi.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.veidz.financeapi.application.dto.CreateTransactionRequest;
import com.veidz.financeapi.application.dto.TransactionResponse;
import com.veidz.financeapi.application.ports.CategoryRepository;
import com.veidz.financeapi.application.ports.TransactionRepository;
import com.veidz.financeapi.application.ports.UserRepository;
import com.veidz.financeapi.domain.entities.Category;
import com.veidz.financeapi.domain.entities.Transaction;
import com.veidz.financeapi.domain.entities.User;
import com.veidz.financeapi.domain.enums.TransactionType;
import com.veidz.financeapi.domain.valueobjects.Email;
import com.veidz.financeapi.domain.valueobjects.Money;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) @DisplayName("CreateTransactionUseCase Tests")
class CreateTransactionUseCaseTest {

  @Mock
  private TransactionRepository transactionRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private CategoryRepository categoryRepository;

  private CreateTransactionUseCase createTransactionUseCase;

  @BeforeEach
  void setUp() {
    createTransactionUseCase = new CreateTransactionUseCase(transactionRepository, userRepository,
        categoryRepository);
  }

  @Nested @DisplayName("Successful Transaction Creation")
  class SuccessfulCreation {

    @Test @DisplayName("should create transaction with all required fields")
    void shouldCreateTransactionWithAllFields() {
      // Given
      User user = User.create("John Doe", new Email("john@example.com"), "password");
      UUID userId = user.getId();
      UUID categoryId = UUID.randomUUID();
      Category category = Category.create(userId, "Food", TransactionType.EXPENSE);

      Money amount = new Money(new BigDecimal("100.00"), java.util.Currency.getInstance("BRL"));
      LocalDate date = LocalDate.of(2025, 11, 11);

      when(userRepository.findById(userId)).thenReturn(Optional.of(user));
      when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

      Transaction savedTransaction = Transaction.create(userId, amount, TransactionType.EXPENSE,
          "Lunch", date.atStartOfDay());
      savedTransaction.assignCategory(categoryId);
      when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

      CreateTransactionRequest request = new CreateTransactionRequest(userId,
          new BigDecimal("100.00"), TransactionType.EXPENSE, "Lunch", date, categoryId);

      // When
      TransactionResponse response = createTransactionUseCase.execute(request);

      // Then
      assertNotNull(response);
      assertEquals(savedTransaction.getId(), response.id());
      assertEquals(userId, response.userId());
      assertEquals(new BigDecimal("100.00"), response.amount());
      assertEquals(TransactionType.EXPENSE, response.type());
      assertEquals("Lunch", response.description());
      assertEquals(date, response.date());
      assertEquals(categoryId, response.categoryId());

      verify(userRepository).findById(userId);
      verify(categoryRepository).findById(categoryId);
      verify(transactionRepository).save(any(Transaction.class));
    }

    @Test @DisplayName("should create transaction without category")
    void shouldCreateTransactionWithoutCategory() {
      // Given
      User user = User.create("John Doe", new Email("john@example.com"), "password");
      UUID userId = user.getId();

      Money amount = new Money(new BigDecimal("50.00"), java.util.Currency.getInstance("BRL"));
      LocalDate date = LocalDate.now();

      when(userRepository.findById(userId)).thenReturn(Optional.of(user));

      Transaction savedTransaction = Transaction.create(user.getId(), amount,
          TransactionType.INCOME, "Salary", date.atStartOfDay());
      when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

      CreateTransactionRequest request = new CreateTransactionRequest(userId,
          new BigDecimal("50.00"), TransactionType.INCOME, "Salary", date, null);

      // When
      TransactionResponse response = createTransactionUseCase.execute(request);

      // Then
      assertNotNull(response);
      assertNull(response.categoryId());
      verify(userRepository).findById(userId);
      verify(categoryRepository, never()).findById(any(UUID.class));
      verify(transactionRepository).save(any(Transaction.class));
    }
  }

  @Nested @DisplayName("Validation Errors")
  class ValidationErrors {

    @Test @DisplayName("should throw exception when amount is zero")
    void shouldThrowExceptionForZeroAmount() {
      // Given
      UUID userId = UUID.randomUUID();
      CreateTransactionRequest request = new CreateTransactionRequest(userId, BigDecimal.ZERO,
          TransactionType.EXPENSE, "Test", LocalDate.now(), null);

      // When & Then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> createTransactionUseCase.execute(request));

      assertTrue(exception.getMessage().toLowerCase().contains("amount"));
      verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test @DisplayName("should throw exception when amount is negative")
    void shouldThrowExceptionForNegativeAmount() {
      // Given
      UUID userId = UUID.randomUUID();
      CreateTransactionRequest request = new CreateTransactionRequest(userId,
          new BigDecimal("-10.00"), TransactionType.EXPENSE, "Test", LocalDate.now(), null);

      // When & Then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> createTransactionUseCase.execute(request));

      assertTrue(exception.getMessage().toLowerCase().contains("amount"));
      verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test @DisplayName("should throw exception when user does not exist")
    void shouldThrowExceptionForNonExistentUser() {
      // Given
      UUID userId = UUID.randomUUID();
      when(userRepository.findById(userId)).thenReturn(Optional.empty());

      CreateTransactionRequest request = new CreateTransactionRequest(userId,
          new BigDecimal("100.00"), TransactionType.EXPENSE, "Test", LocalDate.now(), null);

      // When & Then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> createTransactionUseCase.execute(request));

      assertEquals("User not found", exception.getMessage());
      verify(userRepository).findById(userId);
      verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test @DisplayName("should throw exception when category does not exist")
    void shouldThrowExceptionForNonExistentCategory() {
      // Given
      UUID userId = UUID.randomUUID();
      UUID categoryId = UUID.randomUUID();
      User user = User.create("John Doe", new Email("john@example.com"), "password");

      when(userRepository.findById(userId)).thenReturn(Optional.of(user));
      when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

      CreateTransactionRequest request = new CreateTransactionRequest(userId,
          new BigDecimal("100.00"), TransactionType.EXPENSE, "Test", LocalDate.now(), categoryId);

      // When & Then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> createTransactionUseCase.execute(request));

      assertEquals("Category not found", exception.getMessage());
      verify(userRepository).findById(userId);
      verify(categoryRepository).findById(categoryId);
      verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test @DisplayName("should throw exception when category belongs to different user")
    void shouldThrowExceptionForCategoryBelongingToDifferentUser() {
      // Given
      User user = User.create("John Doe", new Email("john@example.com"), "password");
      UUID userId = user.getId();
      UUID otherUserId = UUID.randomUUID();
      UUID categoryId = UUID.randomUUID();

      Category category = Category.create(otherUserId, "Food", TransactionType.EXPENSE); // Different
                                                                                         // user's
                                                                                         // category

      when(userRepository.findById(userId)).thenReturn(Optional.of(user));
      when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

      CreateTransactionRequest request = new CreateTransactionRequest(userId,
          new BigDecimal("100.00"), TransactionType.EXPENSE, "Test", LocalDate.now(), categoryId);

      // When & Then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> createTransactionUseCase.execute(request));

      assertEquals("Category does not belong to user", exception.getMessage());
      verify(userRepository).findById(userId);
      verify(categoryRepository).findById(categoryId);
      verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test @DisplayName("should throw exception when required fields are null")
    void shouldThrowExceptionForNullFields() {
      // Given
      CreateTransactionRequest request = new CreateTransactionRequest(null, null, null, null, null,
          null);

      // When & Then
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> createTransactionUseCase.execute(request));

      assertTrue(exception.getMessage().contains("required"));
      verify(transactionRepository, never()).save(any(Transaction.class));
    }
  }
}
