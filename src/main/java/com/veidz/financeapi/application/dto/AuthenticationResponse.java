package com.veidz.financeapi.application.dto;

import java.util.UUID;

/**
 * Response DTO for successful authentication.
 *
 * @param userId the authenticated user's unique identifier
 * @param name the user's name
 * @param email the user's email address
 * @param token JWT authentication token (to be added when JWT is implemented)
 */
public record AuthenticationResponse(UUID userId, String name, String email, String token) {}
