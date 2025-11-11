package com.veidz.financeapi.application.dto;

/**
 * Request DTO for user authentication.
 *
 * @param email the user's email address
 * @param password the user's password
 */
public record AuthenticationRequest(String email, String password) {}
