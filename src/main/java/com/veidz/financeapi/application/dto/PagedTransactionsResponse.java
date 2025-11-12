package com.veidz.financeapi.application.dto;

import java.util.List;

/**
 * Response DTO for paginated list of transactions.
 *
 * @param transactions list of transactions in the current page
 * @param totalElements total number of transactions matching the criteria
 * @param totalPages total number of pages
 * @param currentPage current page number (0-based)
 * @param pageSize number of items per page
 * @author Veidz
 */
public record PagedTransactionsResponse(List<TransactionResponse> transactions, long totalElements,
    int totalPages, int currentPage, int pageSize) {
}
