package com.veidz.financeapi.application.usecases;

import com.veidz.financeapi.application.dto.CreateTransactionRequest;
import com.veidz.financeapi.application.dto.TransactionResponse;
import com.veidz.financeapi.application.ports.CategoryRepository;
import com.veidz.financeapi.application.ports.TransactionRepository;
import com.veidz.financeapi.application.ports.UserRepository;
import com.veidz.financeapi.domain.entities.Category;
import com.veidz.financeapi.domain.entities.Transaction;
import com.veidz.financeapi.domain.entities.User;
import com.veidz.financeapi.domain.valueobjects.Money;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

/**
 * Use case for creating a new transaction.
 *
 * @author Veidz
 */
public class CreateTransactionUseCase {

  private final TransactionRepository transactionRepository;
  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;

  /**
   * Constructor for CreateTransactionUseCase.
   *
   * @param transactionRepository the transaction repository
   * @param userRepository the user repository
   * @param categoryRepository the category repository
   * @throws IllegalArgumentException if any repository is null
   */
  @SuppressFBWarnings(value = { "EI_EXPOSE_REP2",
      "CT_CONSTRUCTOR_THROW" }, justification = "Dependencies are injected by framework and exceptions in constructors are acceptable for validation")
  public CreateTransactionUseCase(TransactionRepository transactionRepository,
      UserRepository userRepository, CategoryRepository categoryRepository) {
    if (transactionRepository == null) {
      throw new IllegalArgumentException("Transaction repository cannot be null");
    }
    if (userRepository == null) {
      throw new IllegalArgumentException("User repository cannot be null");
    }
    if (categoryRepository == null) {
      throw new IllegalArgumentException("Category repository cannot be null");
    }
    this.transactionRepository = transactionRepository;
    this.userRepository = userRepository;
    this.categoryRepository = categoryRepository;
  }

  /**
   * Executes the use case to create a new transaction.
   *
   * @param request the create transaction request
   * @return the transaction response with created transaction data
   * @throws IllegalArgumentException if validation fails
   */
  public TransactionResponse execute(CreateTransactionRequest request) {
    validateRequest(request);

    // Verify user exists
    Optional<User> userOptional = userRepository.findById(request.userId());
    if (userOptional.isEmpty()) {
      throw new IllegalArgumentException("User not found");
    }

    // Verify category if provided
    if (request.categoryId() != null) {
      Optional<Category> categoryOptional = categoryRepository.findById(request.categoryId());
      if (categoryOptional.isEmpty()) {
        throw new IllegalArgumentException("Category not found");
      }

      Category category = categoryOptional.get();
      if (!category.getUserId().equals(request.userId())) {
        throw new IllegalArgumentException("Category does not belong to user");
      }
    }

    // Create Money value object with BRL currency (could be parameterized later)
    Money amount = new Money(request.amount(), Currency.getInstance("BRL"));

    // Create Transaction entity
    Transaction transaction = Transaction.create(request.userId(), amount, request.type(),
        request.description(), request.date().atStartOfDay());

    // Assign category if provided
    if (request.categoryId() != null) {
      transaction.assignCategory(request.categoryId());
    }

    // Save transaction
    Transaction savedTransaction = transactionRepository.save(transaction);

    // Map to response
    return mapToResponse(savedTransaction);
  }

  /**
   * Validates the create transaction request.
   *
   * @param request the request to validate
   * @throws IllegalArgumentException if validation fails
   */
  private void validateRequest(CreateTransactionRequest request) {
    if (request.userId() == null) {
      throw new IllegalArgumentException("User ID is required");
    }
    if (request.amount() == null) {
      throw new IllegalArgumentException("Amount is required");
    }
    if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Amount must be greater than zero");
    }
    if (request.type() == null) {
      throw new IllegalArgumentException("Transaction type is required");
    }
    if (request.description() == null || request.description().trim().isEmpty()) {
      throw new IllegalArgumentException("Description is required");
    }
    if (request.date() == null) {
      throw new IllegalArgumentException("Date is required");
    }
  }

  /**
   * Maps a Transaction entity to a TransactionResponse DTO.
   *
   * @param transaction the transaction entity
   * @return the transaction response DTO
   */
  private TransactionResponse mapToResponse(Transaction transaction) {
    return new TransactionResponse(transaction.getId(), transaction.getUserId(),
        transaction.getBalanceImpact().getAmount(), transaction.getType(),
        transaction.getDescription(), transaction.getTransactionDate().toLocalDate(),
        transaction.getCategoryId(), transaction.getCreatedAt());
  }
}
