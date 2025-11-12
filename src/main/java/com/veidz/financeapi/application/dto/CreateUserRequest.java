package com.veidz.financeapi.application.dto;

/**
 * Request DTO for creating a new user.
 *
 * @param name user's full name
 * @param email user's email address
 * @param password user's password (will be hashed)
 * @author Veidz
 */
public record CreateUserRequest(String name, String email, String password) {
}
