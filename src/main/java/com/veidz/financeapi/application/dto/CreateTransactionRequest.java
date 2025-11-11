package com.veidz.financeapi.application.dto;

import com.veidz.financeapi.domain.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Request DTO for creating a transaction.
 *
 * @param userId the user's unique identifier
 * @param amount the transaction amount
 * @param type the transaction type (INCOME or EXPENSE)
 * @param description the transaction description
 * @param date the transaction date
 * @param categoryId the category's unique identifier (optional)
 */
public record CreateTransactionRequest(UUID userId, BigDecimal amount, TransactionType type,
    String description, LocalDate date, UUID categoryId) {
}
