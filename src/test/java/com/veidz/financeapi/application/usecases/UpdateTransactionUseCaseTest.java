package com.veidz.financeapi.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.veidz.financeapi.application.dto.TransactionResponse;
import com.veidz.financeapi.application.dto.UpdateTransactionRequest;
import com.veidz.financeapi.application.ports.CategoryRepository;
import com.veidz.financeapi.application.ports.TransactionRepository;
import com.veidz.financeapi.application.ports.UserRepository;
import com.veidz.financeapi.domain.entities.Category;
import com.veidz.financeapi.domain.entities.Transaction;
import com.veidz.financeapi.domain.enums.TransactionType;
import com.veidz.financeapi.domain.valueobjects.Money;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("UpdateTransactionUseCase")
class UpdateTransactionUseCaseTest {

  private TransactionRepository transactionRepository;
  private UserRepository userRepository;
  private CategoryRepository categoryRepository;
  private UpdateTransactionUseCase useCase;

  @BeforeEach
  void setUp() {
    transactionRepository = mock(TransactionRepository.class);
    userRepository = mock(UserRepository.class);
    categoryRepository = mock(CategoryRepository.class);
    useCase = new UpdateTransactionUseCase(transactionRepository, userRepository,
        categoryRepository);
  }

  @Nested @DisplayName("Constructor Validation")
  class ConstructorValidation {

    @Test @DisplayName("should throw exception when transaction repository is null")
    void shouldThrowExceptionForNullTransactionRepository() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new UpdateTransactionUseCase(null, userRepository, categoryRepository));

      assertEquals("Transaction repository cannot be null", exception.getMessage());
    }

    @Test @DisplayName("should throw exception when user repository is null")
    void shouldThrowExceptionForNullUserRepository() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new UpdateTransactionUseCase(transactionRepository, null, categoryRepository));

      assertEquals("User repository cannot be null", exception.getMessage());
    }

    @Test @DisplayName("should throw exception when category repository is null")
    void shouldThrowExceptionForNullCategoryRepository() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new UpdateTransactionUseCase(transactionRepository, userRepository, null));

      assertEquals("Category repository cannot be null", exception.getMessage());
    }
  }

  @Nested @DisplayName("Successful Updates")
  class SuccessfulUpdates {

    private UUID transactionId;
    private UUID userId;
    private Transaction existingTransaction;

    @BeforeEach
    void setUp() {
      transactionId = UUID.randomUUID();
      userId = UUID.randomUUID();

      Money amount = new Money(new BigDecimal("100.00"), Currency.getInstance("BRL"));
      existingTransaction = Transaction.create(userId, amount, TransactionType.INCOME, "Salary",
          LocalDate.of(2024, 1, 15).atStartOfDay());

      when(transactionRepository.findById(transactionId))
          .thenReturn(Optional.of(existingTransaction));
      when(transactionRepository.save(any(Transaction.class))).thenReturn(existingTransaction);
    }

    @Test @DisplayName("should update transaction description")
    void shouldUpdateTransactionDescription() {
      // Arrange
      String newDescription = "Bonus payment";
      UpdateTransactionRequest request = new UpdateTransactionRequest(transactionId, null, null,
          newDescription, null, null);

      // Act
      TransactionResponse response = useCase.execute(request);

      // Assert
      assertNotNull(response);
      verify(transactionRepository).save(any(Transaction.class));
    }

    @Test @DisplayName("should update transaction category")
    void shouldUpdateTransactionCategory() {
      // Arrange
      UUID categoryId = UUID.randomUUID();
      Category category = Category.create(userId, "Food", TransactionType.EXPENSE);

      when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

      UpdateTransactionRequest request = new UpdateTransactionRequest(transactionId, null, null,
          null, null, categoryId);

      // Act
      TransactionResponse response = useCase.execute(request);

      // Assert
      assertNotNull(response);
      verify(categoryRepository).findById(categoryId);
      verify(transactionRepository).save(any(Transaction.class));
    }

    @Test @DisplayName("should not save when no mutable fields provided")
    void shouldNotSaveWhenNoMutableFieldsProvided() {
      // Arrange
      UpdateTransactionRequest request = new UpdateTransactionRequest(transactionId, null, null,
          null, null, null);

      // Act
      TransactionResponse response = useCase.execute(request);

      // Assert
      assertNotNull(response);
      verify(transactionRepository).save(any(Transaction.class));
    }
  }

  @Nested @DisplayName("Immutable Field Updates")
  class ImmutableFieldUpdates {

    private UUID transactionId;
    private UUID userId;
    private Transaction existingTransaction;

    @BeforeEach
    void setUp() {
      transactionId = UUID.randomUUID();
      userId = UUID.randomUUID();

      Money amount = new Money(new BigDecimal("100.00"), Currency.getInstance("BRL"));
      existingTransaction = Transaction.create(userId, amount, TransactionType.INCOME, "Salary",
          LocalDate.of(2024, 1, 15).atStartOfDay());

      when(transactionRepository.findById(transactionId))
          .thenReturn(Optional.of(existingTransaction));
    }

    @Test @DisplayName("should throw exception when trying to update amount")
    void shouldThrowExceptionWhenTryingToUpdateAmount() {
      // Arrange
      UpdateTransactionRequest request = new UpdateTransactionRequest(transactionId,
          new BigDecimal("200.00"), null, null, null, null);

      // Act & Assert
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> useCase.execute(request));

      assertEquals("Cannot update amount - field is immutable. Create a new transaction instead.",
          exception.getMessage());
      verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test @DisplayName("should throw exception when trying to update type")
    void shouldThrowExceptionWhenTryingToUpdateType() {
      // Arrange
      UpdateTransactionRequest request = new UpdateTransactionRequest(transactionId, null,
          TransactionType.EXPENSE, null, null, null);

      // Act & Assert
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> useCase.execute(request));

      assertEquals("Cannot update type - field is immutable. Create a new transaction instead.",
          exception.getMessage());
      verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test @DisplayName("should throw exception when trying to update date")
    void shouldThrowExceptionWhenTryingToUpdateDate() {
      // Arrange
      UpdateTransactionRequest request = new UpdateTransactionRequest(transactionId, null, null,
          null, LocalDate.of(2024, 2, 1), null);

      // Act & Assert
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> useCase.execute(request));

      assertEquals("Cannot update date - field is immutable. Create a new transaction instead.",
          exception.getMessage());
      verify(transactionRepository, never()).save(any(Transaction.class));
    }
  }

  @Nested @DisplayName("Validation Errors")
  class ValidationErrors {

    @Test @DisplayName("should throw exception when request is null")
    void shouldThrowExceptionWhenRequestIsNull() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> useCase.execute(null));

      assertEquals("Request cannot be null", exception.getMessage());
    }

    @Test @DisplayName("should throw exception when transaction id is null")
    void shouldThrowExceptionWhenTransactionIdIsNull() {
      UpdateTransactionRequest request = new UpdateTransactionRequest(null, null, null, "Updated",
          null, null);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> useCase.execute(request));

      assertEquals("Transaction ID cannot be null", exception.getMessage());
    }

    @Test @DisplayName("should throw exception when transaction does not exist")
    void shouldThrowExceptionWhenTransactionDoesNotExist() {
      UUID transactionId = UUID.randomUUID();
      when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

      UpdateTransactionRequest request = new UpdateTransactionRequest(transactionId, null, null,
          "Updated", null, null);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> useCase.execute(request));

      assertEquals("Transaction not found with id: " + transactionId, exception.getMessage());
    }

    @Test @DisplayName("should throw exception when category does not exist")
    void shouldThrowExceptionWhenCategoryDoesNotExist() {
      UUID transactionId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      UUID categoryId = UUID.randomUUID();

      Money amount = new Money(new BigDecimal("100.00"), Currency.getInstance("BRL"));
      Transaction existingTransaction = Transaction.create(userId, amount, TransactionType.INCOME,
          "Salary", LocalDate.of(2024, 1, 15).atStartOfDay());

      when(transactionRepository.findById(transactionId))
          .thenReturn(Optional.of(existingTransaction));
      when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

      UpdateTransactionRequest request = new UpdateTransactionRequest(transactionId, null, null,
          null, null, categoryId);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> useCase.execute(request));

      assertEquals("Category not found with id: " + categoryId, exception.getMessage());
    }

    @Test @DisplayName("should throw exception when category does not belong to user")
    void shouldThrowExceptionWhenCategoryDoesNotBelongToUser() {
      UUID transactionId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();
      UUID categoryId = UUID.randomUUID();
      UUID otherUserId = UUID.randomUUID();

      Money amount = new Money(new BigDecimal("100.00"), Currency.getInstance("BRL"));
      Transaction existingTransaction = Transaction.create(userId, amount, TransactionType.INCOME,
          "Salary", LocalDate.of(2024, 1, 15).atStartOfDay());
      Category category = Category.create(otherUserId, "Food", TransactionType.EXPENSE);

      when(transactionRepository.findById(transactionId))
          .thenReturn(Optional.of(existingTransaction));
      when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

      UpdateTransactionRequest request = new UpdateTransactionRequest(transactionId, null, null,
          null, null, categoryId);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> useCase.execute(request));

      assertEquals("Category does not belong to the user", exception.getMessage());
    }

    @Test @DisplayName("should throw exception when description is empty")
    void shouldThrowExceptionWhenDescriptionIsEmpty() {
      UUID transactionId = UUID.randomUUID();
      UUID userId = UUID.randomUUID();

      Money amount = new Money(new BigDecimal("100.00"), Currency.getInstance("BRL"));
      Transaction existingTransaction = Transaction.create(userId, amount, TransactionType.INCOME,
          "Salary", LocalDate.of(2024, 1, 15).atStartOfDay());

      when(transactionRepository.findById(transactionId))
          .thenReturn(Optional.of(existingTransaction));

      UpdateTransactionRequest request = new UpdateTransactionRequest(transactionId, null, null, "",
          null, null);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> useCase.execute(request));

      assertEquals("Description cannot be empty", exception.getMessage());
    }
  }
}
