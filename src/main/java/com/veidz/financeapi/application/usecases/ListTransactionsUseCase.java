package com.veidz.financeapi.application.usecases;

import com.veidz.financeapi.application.dto.ListTransactionsRequest;
import com.veidz.financeapi.application.dto.PagedTransactionsResponse;
import com.veidz.financeapi.application.dto.TransactionResponse;
import com.veidz.financeapi.application.ports.TransactionRepository;
import com.veidz.financeapi.application.ports.UserRepository;
import com.veidz.financeapi.domain.entities.Transaction;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.List;

/**
 * Use case for listing transactions with optional filters and pagination.
 *
 * @author Veidz
 */
public class ListTransactionsUseCase {

  private final TransactionRepository transactionRepository;
  private final UserRepository userRepository;

  /**
   * Constructor with dependencies.
   *
   * @param transactionRepository repository for transaction operations
   * @param userRepository repository for user operations
   * @throws IllegalArgumentException if any repository is null
   */
  @SuppressFBWarnings(value = { "EI_EXPOSE_REP2", "CT_CONSTRUCTOR_THROW" })
  public ListTransactionsUseCase(TransactionRepository transactionRepository,
      UserRepository userRepository) {
    if (transactionRepository == null) {
      throw new IllegalArgumentException("Transaction repository cannot be null");
    }
    if (userRepository == null) {
      throw new IllegalArgumentException("User repository cannot be null");
    }
    this.transactionRepository = transactionRepository;
    this.userRepository = userRepository;
  }

  /**
   * Executes the use case to list transactions.
   *
   * @param request the list transactions request with filters
   * @return paginated response with transactions
   * @throws IllegalArgumentException if validation fails
   */
  public PagedTransactionsResponse execute(ListTransactionsRequest request) {
    validateRequest(request);
    validatePagination(request.page(), request.size());
    validateDateRange(request.startDate(), request.endDate());
    validateUser(request.userId());

    List<Transaction> transactions = transactionRepository.findByUserIdWithFilters(request.userId(),
        request.startDate(), request.endDate(), request.type(), request.categoryId(),
        request.page(), request.size());

    long totalElements = transactionRepository.countByUserIdWithFilters(request.userId(),
        request.startDate(), request.endDate(), request.type(), request.categoryId());

    List<TransactionResponse> responses = transactions.stream().map(this::toResponse).toList();

    int totalPages = (int) Math.ceil((double) totalElements / request.size());

    return new PagedTransactionsResponse(responses, totalElements, totalPages, request.page(),
        request.size());
  }

  private void validateRequest(ListTransactionsRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Request cannot be null");
    }
    if (request.userId() == null) {
      throw new IllegalArgumentException("User ID cannot be null");
    }
  }

  private void validateUser(java.util.UUID userId) {
    if (userRepository.findById(userId).isEmpty()) {
      throw new IllegalArgumentException("User not found with id: " + userId);
    }
  }

  private void validatePagination(int page, int size) {
    if (page < 0) {
      throw new IllegalArgumentException("Page number cannot be negative");
    }
    if (size <= 0) {
      throw new IllegalArgumentException("Page size must be greater than zero");
    }
  }

  private void validateDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate) {
    if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date cannot be after end date");
    }
  }

  private TransactionResponse toResponse(Transaction transaction) {
    return new TransactionResponse(transaction.getId(), transaction.getUserId(),
        transaction.getAmount().getAmount(), transaction.getType(), transaction.getDescription(),
        transaction.getTransactionDate().toLocalDate(), transaction.getCategoryId(),
        transaction.getCreatedAt());
  }
}
