package com.veidz.financeapi.domain.entities;

import com.veidz.financeapi.domain.valueobjects.Email;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * User domain entity representing a user in the system.
 * Contains business logic and invariants for user management.
 *
 * @author Veidz
 */
public class User {

  private final UUID id;
  private String name;
  private final Email email;
  private String passwordHash;
  private final LocalDateTime createdAt;

  /**
   * Creates a new User with the given details.
   *
   * @param name the user's name (must be non-null and non-empty)
   * @param email the user's email (must be non-null)
   * @param plainPassword the plain text password (must be at least 6 characters)
   * @throws IllegalArgumentException if any validation fails
   */
  public User(String name, Email email, String plainPassword) {
    validateName(name);
    validateEmail(email);
    validatePassword(plainPassword);

    this.id = UUID.randomUUID();
    this.name = name.trim();
    this.email = email;
    this.passwordHash = hashPassword(plainPassword);
    this.createdAt = LocalDateTime.now();
  }

  /**
   * Verifies if the provided plain password matches the stored hash.
   *
   * @param plainPassword the password to verify
   * @return true if the password matches, false otherwise
   */
  public boolean verifyPassword(String plainPassword) {
    if (plainPassword == null) {
      return false;
    }
    return this.passwordHash.equals(hashPassword(plainPassword));
  }

  /**
   * Updates the user's name.
   *
   * @param newName the new name (must be non-null and non-empty)
   * @throws IllegalArgumentException if the new name is invalid
   */
  public void updateName(String newName) {
    validateName(newName);
    this.name = newName.trim();
  }

  /**
   * Changes the user's password.
   *
   * @param currentPassword the current password for verification
   * @param newPassword the new password (must be at least 6 characters)
   * @throws IllegalArgumentException if current password is wrong or new password is invalid
   */
  public void changePassword(String currentPassword, String newPassword) {
    if (!verifyPassword(currentPassword)) {
      throw new IllegalArgumentException("Current password is incorrect");
    }
    validatePassword(newPassword);
    this.passwordHash = hashPassword(newPassword);
  }

  private void validateName(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be null or empty");
    }
  }

  private void validateEmail(Email email) {
    if (email == null) {
      throw new IllegalArgumentException("Email cannot be null");
    }
  }

  private void validatePassword(String password) {
    if (password == null || password.isEmpty()) {
      throw new IllegalArgumentException("Password cannot be null or empty");
    }
    if (password.length() < 6) {
      throw new IllegalArgumentException("Password must be at least 6 characters long");
    }
  }

  /**
   * Simple password hashing (for demonstration purposes).
   * In production, use BCrypt or similar secure hashing algorithms.
   *
   * @param plainPassword the plain text password
   * @return the hashed password
   */
  private String hashPassword(String plainPassword) {
    // Simple hash for now (will be replaced with BCrypt in infrastructure layer)
    return "hashed_" + plainPassword + "_" + plainPassword.length();
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Email getEmail() {
    return email;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "User{id=" + id + ", name='" + name + "', email=" + email + "}";
  }
}
