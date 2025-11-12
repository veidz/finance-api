package com.veidz.financeapi.application.dto;

import com.veidz.financeapi.domain.enums.TransactionType;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Request DTO for listing transactions.
 *
 * @param userId the user ID who owns the transactions
 * @param startDate optional start date for filtering transactions
 * @param endDate optional end date for filtering transactions
 * @param type optional transaction type filter (INCOME or EXPENSE)
 * @param categoryId optional category ID filter
 * @param page page number for pagination (0-based)
 * @param size number of items per page
 * @author Veidz
 */
public record ListTransactionsRequest(UUID userId, LocalDate startDate, LocalDate endDate,
    TransactionType type, UUID categoryId, int page, int size) {
}
