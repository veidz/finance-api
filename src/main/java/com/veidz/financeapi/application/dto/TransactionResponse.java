package com.veidz.financeapi.application.dto;

import com.veidz.financeapi.domain.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for transaction operations.
 *
 * @param id the transaction's unique identifier
 * @param userId the user's unique identifier
 * @param amount the transaction amount
 * @param type the transaction type (INCOME or EXPENSE)
 * @param description the transaction description
 * @param date the transaction date
 * @param categoryId the category's unique identifier (optional)
 * @param createdAt the transaction creation timestamp
 */
public record TransactionResponse(UUID id, UUID userId, BigDecimal amount, TransactionType type,
    String description, LocalDate date, UUID categoryId, LocalDateTime createdAt) {
}
