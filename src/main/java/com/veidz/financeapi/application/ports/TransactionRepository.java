package com.veidz.financeapi.application.ports;

import com.veidz.financeapi.domain.entities.Transaction;
import com.veidz.financeapi.domain.enums.TransactionType;
import java.time.LocalDate;
import java.util.List;
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

  /**
   * Finds transactions for a user with optional filters and pagination.
   *
   * @param userId the user ID who owns the transactions
   * @param startDate optional start date for filtering (inclusive)
   * @param endDate optional end date for filtering (inclusive)
   * @param type optional transaction type filter
   * @param categoryId optional category ID filter
   * @param page page number (0-based)
   * @param size number of items per page
   * @return list of transactions matching the criteria
   */
  List<Transaction> findByUserIdWithFilters(UUID userId, LocalDate startDate, LocalDate endDate,
      TransactionType type, UUID categoryId, int page, int size);

  /**
   * Counts transactions for a user with optional filters.
   *
   * @param userId the user ID who owns the transactions
   * @param startDate optional start date for filtering (inclusive)
   * @param endDate optional end date for filtering (inclusive)
   * @param type optional transaction type filter
   * @param categoryId optional category ID filter
   * @return total count of transactions matching the criteria
   */
  long countByUserIdWithFilters(UUID userId, LocalDate startDate, LocalDate endDate,
      TransactionType type, UUID categoryId);
}
