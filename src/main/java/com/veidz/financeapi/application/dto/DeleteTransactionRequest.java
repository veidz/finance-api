package com.veidz.financeapi.application.dto;

import java.util.UUID;

/**
 * Request DTO for deleting a transaction.
 *
 * @param transactionId the ID of the transaction to delete
 */
public record DeleteTransactionRequest(UUID transactionId) {
}
