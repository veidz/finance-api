package com.veidz.financeapi.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for user data.
 *
 * @param id user's unique identifier
 * @param name user's full name
 * @param email user's email address
 * @param createdAt timestamp when user was created
 * @author Veidz
 */
public record UserResponse(UUID id, String name, String email, LocalDateTime createdAt) {
}
