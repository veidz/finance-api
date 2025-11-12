package com.veidz.financeapi.application.ports;

import com.veidz.financeapi.domain.entities.Transaction;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for {@link Transaction} entity.
 *
 * <p>
 * This port defines the contract for persisting and retrieving transactions. Implementations should
 * be provided in the infrastructure layer.
 */
public interface TransactionRepository {

  /**
   * Saves a transaction to the repository.
   *
   * @param transaction the transaction to save
   * @return the saved transaction with generated ID if new
   */
  Transaction save(Transaction transaction);

  /**
   * Finds a transaction by its unique identifier.
   *
   * @param id the transaction's ID
   * @return an Optional containing the transaction if found, empty otherwise
   */
  Optional<Transaction> findById(UUID id);

  /**
   * Deletes a transaction by its unique identifier.
   *
   * @param id the transaction's ID
   */
  void deleteById(UUID id);
}
