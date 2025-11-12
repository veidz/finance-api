package com.veidz.financeapi.application.dto;

import com.veidz.financeapi.domain.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Request DTO for updating a transaction. All fields except transactionId are optional - only
 * provided fields will be updated.
 *
 * @param transactionId the transaction ID to update (required)
 * @param amount optional new transaction amount
 * @param type optional new transaction type
 * @param description optional new transaction description
 * @param date optional new transaction date
 * @param categoryId optional new category ID (can be null to remove category)
 * @author Veidz
 */
public record UpdateTransactionRequest(UUID transactionId, BigDecimal amount, TransactionType type,
    String description, LocalDate date, UUID categoryId) {
}
