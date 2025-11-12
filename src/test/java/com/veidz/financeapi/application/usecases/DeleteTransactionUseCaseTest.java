package com.veidz.financeapi.application.usecases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.veidz.financeapi.application.dto.DeleteTransactionRequest;
import com.veidz.financeapi.application.ports.TransactionRepository;
import com.veidz.financeapi.domain.entities.Transaction;
import com.veidz.financeapi.domain.enums.TransactionType;
import com.veidz.financeapi.domain.valueobjects.Money;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DeleteTransactionUseCaseTest {

  private TransactionRepository transactionRepository;
  private DeleteTransactionUseCase deleteTransactionUseCase;

  @BeforeEach
  void setUp() {
    transactionRepository = mock(TransactionRepository.class);
    deleteTransactionUseCase = new DeleteTransactionUseCase(transactionRepository);
  }

  @Nested
  class ConstructorValidation {

    @Test
    void shouldThrowExceptionWhenTransactionRepositoryIsNull() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> new DeleteTransactionUseCase(null));

      assertEquals("TransactionRepository cannot be null", exception.getMessage());
    }
  }

  @Nested
  class SuccessfulDeletion {

    @Test
    void shouldDeleteTransactionWhenTransactionExists() {
      UUID userId = UUID.randomUUID();
      UUID transactionId = UUID.randomUUID();

      Transaction transaction = Transaction.create(userId,
          new Money(BigDecimal.valueOf(100.00), Currency.getInstance("USD")),
          TransactionType.EXPENSE, "Test expense", LocalDate.now().atStartOfDay());

      DeleteTransactionRequest request = new DeleteTransactionRequest(transactionId);

      when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

      assertDoesNotThrow(() -> deleteTransactionUseCase.execute(request));

      verify(transactionRepository).deleteById(transactionId);
    }
  }

  @Nested
  class ValidationErrors {

    @Test
    void shouldThrowExceptionWhenRequestIsNull() {
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> deleteTransactionUseCase.execute(null));

      assertEquals("Request cannot be null", exception.getMessage());
      verify(transactionRepository, never()).deleteById(any());
    }

    @Test
    void shouldThrowExceptionWhenTransactionIdIsNull() {
      DeleteTransactionRequest request = new DeleteTransactionRequest(null);

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> deleteTransactionUseCase.execute(request));

      assertEquals("Transaction ID cannot be null", exception.getMessage());
      verify(transactionRepository, never()).deleteById(any());
    }

    @Test
    void shouldThrowExceptionWhenTransactionDoesNotExist() {
      UUID transactionId = UUID.randomUUID();
      DeleteTransactionRequest request = new DeleteTransactionRequest(transactionId);

      when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> deleteTransactionUseCase.execute(request));

      assertEquals("Transaction not found with id: " + transactionId, exception.getMessage());
      verify(transactionRepository, never()).deleteById(any());
    }
  }
}
