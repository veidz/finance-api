package com.veidz.financeapi.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.veidz.financeapi.application.dto.ListTransactionsRequest;
import com.veidz.financeapi.application.dto.PagedTransactionsResponse;
import com.veidz.financeapi.application.ports.TransactionRepository;
import com.veidz.financeapi.application.ports.UserRepository;
import com.veidz.financeapi.domain.entities.Category;
import com.veidz.financeapi.domain.entities.Transaction;
import com.veidz.financeapi.domain.enums.TransactionType;
import com.veidz.financeapi.domain.valueobjects.Money;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Currency;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ListTransactionsUseCase")
class ListTransactionsUseCaseTest {

  private TransactionRepository transactionRepository;
  private UserRepository userRepository;
  private ListTransactionsUseCase useCase;

  @BeforeEach
  void setUp() {
    transactionRepository = mock(TransactionRepository.class);
    userRepository = mock(UserRepository.class);
    useCase = new ListTransactionsUseCase(transactionRepository, userRepository);
  }

  @Nested @DisplayName("Constructor Validation")
  class ConstructorValidation {

    @Test @DisplayName("should throw exception when transaction repository is null")
    void shouldThrowExceptionForNullTransactionRepository() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new ListTransactionsUseCase(null, userRepository));

      assertEquals("Transaction repository cannot be null", exception.getMessage());
    }

    @Test @DisplayName("should throw exception when user repository is null")
    void shouldThrowExceptionForNullUserRepository() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new ListTransactionsUseCase(transactionRepository, null));

      assertEquals("User repository cannot be null", exception.getMessage());
    }
  }

  @Nested @DisplayName("Successful Listing")
  class SuccessfulListing {

    private UUID userId;
    private Transaction transaction1;
    private Transaction transaction2;

    @BeforeEach
    void setUp() {
      userId = UUID.randomUUID();

      // Mock user exists
      when(userRepository.findById(userId)).thenReturn(Optional.of(mock()));

      // Create sample transactions
      Money amount1 = new Money(new BigDecimal("100.00"), Currency.getInstance("BRL"));
      transaction1 = Transaction.create(userId, amount1, TransactionType.INCOME, "Salary",
          LocalDate.of(2024, 1, 15).atStartOfDay());

      Money amount2 = new Money(new BigDecimal("50.00"), Currency.getInstance("BRL"));
      transaction2 = Transaction.create(userId, amount2, TransactionType.EXPENSE, "Groceries",
          LocalDate.of(2024, 1, 20).atStartOfDay());
    }

    @Test @DisplayName("should return all transactions for user without filters")
    void shouldReturnAllTransactionsForUser() {
      // Arrange
      List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
      when(transactionRepository.findByUserIdWithFilters(eq(userId), eq(null), eq(null), eq(null),
          eq(null), eq(0), eq(20))).thenReturn(transactions);
      when(transactionRepository.countByUserIdWithFilters(eq(userId), eq(null), eq(null), eq(null),
          eq(null))).thenReturn(2L);

      ListTransactionsRequest request = new ListTransactionsRequest(userId, null, null, null, null,
          0, 20);

      // Act
      PagedTransactionsResponse response = useCase.execute(request);

      // Assert
      assertNotNull(response);
      assertEquals(2, response.transactions().size());
      assertEquals(2L, response.totalElements());
      assertEquals(1, response.totalPages());
      assertEquals(0, response.currentPage());
      assertEquals(20, response.pageSize());
    }

    @Test @DisplayName("should return empty list when no transactions found")
    void shouldReturnEmptyListWhenNoTransactionsFound() {
      // Arrange
      when(transactionRepository.findByUserIdWithFilters(eq(userId), eq(null), eq(null), eq(null),
          eq(null), eq(0), eq(20))).thenReturn(Collections.emptyList());
      when(transactionRepository.countByUserIdWithFilters(eq(userId), eq(null), eq(null), eq(null),
          eq(null))).thenReturn(0L);

      ListTransactionsRequest request = new ListTransactionsRequest(userId, null, null, null, null,
          0, 20);

      // Act
      PagedTransactionsResponse response = useCase.execute(request);

      // Assert
      assertNotNull(response);
      assertEquals(0, response.transactions().size());
      assertEquals(0L, response.totalElements());
      assertEquals(0, response.totalPages());
    }

    @Test @DisplayName("should filter transactions by date range")
    void shouldFilterTransactionsByDateRange() {
      // Arrange
      LocalDate startDate = LocalDate.of(2024, 1, 1);
      LocalDate endDate = LocalDate.of(2024, 1, 31);

      when(transactionRepository.findByUserIdWithFilters(eq(userId), eq(startDate), eq(endDate),
          eq(null), eq(null), eq(0), eq(20))).thenReturn(Arrays.asList(transaction1));
      when(transactionRepository.countByUserIdWithFilters(eq(userId), eq(startDate), eq(endDate),
          eq(null), eq(null))).thenReturn(1L);

      ListTransactionsRequest request = new ListTransactionsRequest(userId, startDate, endDate,
          null, null, 0, 20);

      // Act
      PagedTransactionsResponse response = useCase.execute(request);

      // Assert
      assertNotNull(response);
      assertEquals(1, response.transactions().size());
      verify(transactionRepository).findByUserIdWithFilters(userId, startDate, endDate, null, null,
          0, 20);
    }

    @Test @DisplayName("should filter transactions by type")
    void shouldFilterTransactionsByType() {
      // Arrange
      when(transactionRepository.findByUserIdWithFilters(eq(userId), eq(null), eq(null),
          eq(TransactionType.INCOME), eq(null), eq(0), eq(20)))
              .thenReturn(Arrays.asList(transaction1));
      when(transactionRepository.countByUserIdWithFilters(eq(userId), eq(null), eq(null),
          eq(TransactionType.INCOME), eq(null))).thenReturn(1L);

      ListTransactionsRequest request = new ListTransactionsRequest(userId, null, null,
          TransactionType.INCOME, null, 0, 20);

      // Act
      PagedTransactionsResponse response = useCase.execute(request);

      // Assert
      assertNotNull(response);
      assertEquals(1, response.transactions().size());
      verify(transactionRepository).findByUserIdWithFilters(userId, null, null,
          TransactionType.INCOME, null, 0, 20);
    }

    @Test @DisplayName("should filter transactions by category")
    void shouldFilterTransactionsByCategory() {
      // Arrange
      UUID categoryId = UUID.randomUUID();

      when(transactionRepository.findByUserIdWithFilters(eq(userId), eq(null), eq(null), eq(null),
          eq(categoryId), eq(0), eq(20))).thenReturn(Arrays.asList(transaction2));
      when(transactionRepository.countByUserIdWithFilters(eq(userId), eq(null), eq(null), eq(null),
          eq(categoryId))).thenReturn(1L);

      ListTransactionsRequest request = new ListTransactionsRequest(userId, null, null, null,
          categoryId, 0, 20);

      // Act
      PagedTransactionsResponse response = useCase.execute(request);

      // Assert
      assertNotNull(response);
      assertEquals(1, response.transactions().size());
      verify(transactionRepository).findByUserIdWithFilters(userId, null, null, null, categoryId, 0,
          20);
    }

    @Test @DisplayName("should apply pagination correctly")
    void shouldApplyPaginationCorrectly() {
      // Arrange
      when(transactionRepository.findByUserIdWithFilters(eq(userId), eq(null), eq(null), eq(null),
          eq(null), eq(1), eq(10))).thenReturn(Collections.emptyList());
      when(transactionRepository.countByUserIdWithFilters(eq(userId), eq(null), eq(null), eq(null),
          eq(null))).thenReturn(25L);

      ListTransactionsRequest request = new ListTransactionsRequest(userId, null, null, null, null,
          1, 10);

      // Act
      PagedTransactionsResponse response = useCase.execute(request);

      // Assert
      assertNotNull(response);
      assertEquals(1, response.currentPage());
      assertEquals(10, response.pageSize());
      assertEquals(3, response.totalPages()); // 25 items / 10 per page = 3 pages
      verify(transactionRepository).findByUserIdWithFilters(userId, null, null, null, null, 1, 10);
    }

    @Test @DisplayName("should apply combined filters")
    void shouldApplyCombinedFilters() {
      // Arrange
      LocalDate startDate = LocalDate.of(2024, 1, 1);
      LocalDate endDate = LocalDate.of(2024, 1, 31);
      UUID categoryId = UUID.randomUUID();

      when(transactionRepository.findByUserIdWithFilters(eq(userId), eq(startDate), eq(endDate),
          eq(TransactionType.EXPENSE), eq(categoryId), eq(0), eq(20)))
              .thenReturn(Arrays.asList(transaction2));
      when(transactionRepository.countByUserIdWithFilters(eq(userId), eq(startDate), eq(endDate),
          eq(TransactionType.EXPENSE), eq(categoryId))).thenReturn(1L);

      ListTransactionsRequest request = new ListTransactionsRequest(userId, startDate, endDate,
          TransactionType.EXPENSE, categoryId, 0, 20);

      // Act
      PagedTransactionsResponse response = useCase.execute(request);

      // Assert
      assertNotNull(response);
      assertEquals(1, response.transactions().size());
      verify(transactionRepository).findByUserIdWithFilters(userId, startDate, endDate,
          TransactionType.EXPENSE, categoryId, 0, 20);
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

    @Test @DisplayName("should throw exception when user id is null")
    void shouldThrowExceptionWhenUserIdIsNull() {
      ListTransactionsRequest request = new ListTransactionsRequest(null, null, null, null, null, 0,
          20);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> useCase.execute(request));

      assertEquals("User ID cannot be null", exception.getMessage());
    }

    @Test @DisplayName("should throw exception when user does not exist")
    void shouldThrowExceptionWhenUserDoesNotExist() {
      UUID userId = UUID.randomUUID();
      when(userRepository.findById(userId)).thenReturn(Optional.empty());

      ListTransactionsRequest request = new ListTransactionsRequest(userId, null, null, null, null,
          0, 20);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> useCase.execute(request));

      assertEquals("User not found with id: " + userId, exception.getMessage());
    }

    @Test @DisplayName("should throw exception when page is negative")
    void shouldThrowExceptionWhenPageIsNegative() {
      UUID userId = UUID.randomUUID();
      ListTransactionsRequest request = new ListTransactionsRequest(userId, null, null, null, null,
          -1, 20);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> useCase.execute(request));

      assertEquals("Page number cannot be negative", exception.getMessage());
    }

    @Test @DisplayName("should throw exception when page size is zero or negative")
    void shouldThrowExceptionWhenPageSizeIsZeroOrNegative() {
      UUID userId = UUID.randomUUID();
      ListTransactionsRequest request = new ListTransactionsRequest(userId, null, null, null, null,
          0, 0);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> useCase.execute(request));

      assertEquals("Page size must be greater than zero", exception.getMessage());
    }

    @Test @DisplayName("should throw exception when start date is after end date")
    void shouldThrowExceptionWhenStartDateIsAfterEndDate() {
      UUID userId = UUID.randomUUID();
      LocalDate startDate = LocalDate.of(2024, 2, 1);
      LocalDate endDate = LocalDate.of(2024, 1, 1);

      ListTransactionsRequest request = new ListTransactionsRequest(userId, startDate, endDate,
          null, null, 0, 20);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> useCase.execute(request));

      assertEquals("Start date cannot be after end date", exception.getMessage());
    }
  }
}
