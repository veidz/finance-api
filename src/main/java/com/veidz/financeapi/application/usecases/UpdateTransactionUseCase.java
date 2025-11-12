package com.veidz.financeapi.application.usecases;

import com.veidz.financeapi.application.dto.TransactionResponse;
import com.veidz.financeapi.application.dto.UpdateTransactionRequest;
import com.veidz.financeapi.application.ports.CategoryRepository;
import com.veidz.financeapi.application.ports.TransactionRepository;
import com.veidz.financeapi.application.ports.UserRepository;
import com.veidz.financeapi.domain.entities.Category;
import com.veidz.financeapi.domain.entities.Transaction;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Use case for updating an existing transaction. Supports partial updates - only provided fields
 * will be updated.
 *
 * @author Veidz
 */
public class UpdateTransactionUseCase {

  private final TransactionRepository transactionRepository;
  private final CategoryRepository categoryRepository;

  /**
   * Constructor with dependencies.
   *
   * @param transactionRepository repository for transaction operations
   * @param userRepository repository for user operations
   * @param categoryRepository repository for category operations
   * @throws IllegalArgumentException if any repository is null
   */
  @SuppressFBWarnings(value = { "EI_EXPOSE_REP2", "CT_CONSTRUCTOR_THROW" })
  public UpdateTransactionUseCase(TransactionRepository transactionRepository,
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
    this.categoryRepository = categoryRepository;
  }

  /**
   * Executes the use case to update a transaction. Only mutable fields (description and category)
   * can be updated. For amount, type, or date changes, create a new transaction instead.
   *
   * @param request the update transaction request
   * @return updated transaction response
   * @throws IllegalArgumentException if validation fails
   */
  public TransactionResponse execute(UpdateTransactionRequest request) {
    validateRequest(request);

    Transaction transaction = transactionRepository.findById(request.transactionId())
        .orElseThrow(() -> new IllegalArgumentException(
            "Transaction not found with id: " + request.transactionId()));

    // Validate immutable field updates are not attempted
    validateImmutableFields(request);

    // Update mutable field: description
    if (request.description() != null) {
      validateDescription(request.description());
      transaction.updateDescription(request.description());
    }

    // Update mutable field: category
    if (request.categoryId() != null) {
      Category category = categoryRepository.findById(request.categoryId())
          .orElseThrow(() -> new IllegalArgumentException(
              "Category not found with id: " + request.categoryId()));

      if (!category.getUserId().equals(transaction.getUserId())) {
        throw new IllegalArgumentException("Category does not belong to the user");
      }

      transaction.assignCategory(request.categoryId());
    }

    Transaction savedTransaction = transactionRepository.save(transaction);

    return new TransactionResponse(savedTransaction.getId(), savedTransaction.getUserId(),
        savedTransaction.getAmount().getAmount(), savedTransaction.getType(),
        savedTransaction.getDescription(), savedTransaction.getTransactionDate().toLocalDate(),
        savedTransaction.getCategoryId(), savedTransaction.getCreatedAt());
  }

  private void validateRequest(UpdateTransactionRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Request cannot be null");
    }
    if (request.transactionId() == null) {
      throw new IllegalArgumentException("Transaction ID cannot be null");
    }
  }

  private void validateImmutableFields(UpdateTransactionRequest request) {
    if (request.amount() != null) {
      throw new IllegalArgumentException(
          "Cannot update amount - field is immutable. Create a new transaction instead.");
    }
    if (request.type() != null) {
      throw new IllegalArgumentException(
          "Cannot update type - field is immutable. Create a new transaction instead.");
    }
    if (request.date() != null) {
      throw new IllegalArgumentException(
          "Cannot update date - field is immutable. Create a new transaction instead.");
    }
  }

  private void validateDescription(String description) {
    if (description.trim().isEmpty()) {
      throw new IllegalArgumentException("Description cannot be empty");
    }
  }
}
