package com.veidz.financeapi.application.usecases;

import com.veidz.financeapi.application.dto.DeleteTransactionRequest;
import com.veidz.financeapi.application.ports.TransactionRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.UUID;

/**
 * Use case for deleting a transaction.
 *
 * <p>
 * This use case handles the deletion of existing transactions.
 */
public class DeleteTransactionUseCase {

  private final TransactionRepository transactionRepository;

  /**
   * Constructs a new DeleteTransactionUseCase.
   *
   * @param transactionRepository the transaction repository
   * @throws IllegalArgumentException if transactionRepository is null
   */
  @SuppressFBWarnings(value = { "EI_EXPOSE_REP2",
      "CT_CONSTRUCTOR_THROW" }, justification = "Repository is injected by DI container, constructor validation is intentional")
  public DeleteTransactionUseCase(TransactionRepository transactionRepository) {
    if (transactionRepository == null) {
      throw new IllegalArgumentException("TransactionRepository cannot be null");
    }
    this.transactionRepository = transactionRepository;
  }

  /**
   * Executes the delete transaction use case.
   *
   * @param request the delete transaction request
   * @throws IllegalArgumentException if validation fails
   */
  public void execute(DeleteTransactionRequest request) {
    validateRequest(request);

    UUID transactionId = request.transactionId();
    transactionRepository.findById(transactionId).orElseThrow(
        () -> new IllegalArgumentException("Transaction not found with id: " + transactionId));

    transactionRepository.deleteById(transactionId);
  }

  private void validateRequest(DeleteTransactionRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Request cannot be null");
    }
    if (request.transactionId() == null) {
      throw new IllegalArgumentException("Transaction ID cannot be null");
    }
  }
}
