package com.veidz.financeapi.domain.valueobjects;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object representing an email address. Immutable and ensures email format validation.
 *
 * @author Veidz
 */
public final class Email {

  private static final Pattern EMAIL_PATTERN = Pattern
      .compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

  private final String value;

  /**
   * Creates an Email instance with the given email address.
   *
   * @param value the email address (must be non-null, non-empty, and valid format)
   * @throws IllegalArgumentException if email is null, empty, or invalid format
   */
  public Email(String value) {
    if (value == null) {
      throw new IllegalArgumentException("Email cannot be null");
    }
    if (value.isBlank()) {
      throw new IllegalArgumentException("Email cannot be empty");
    }
    
    String normalizedEmail = value.trim().toLowerCase(Locale.ROOT);
    
    if (!EMAIL_PATTERN.matcher(normalizedEmail).matches()) {
      throw new IllegalArgumentException("Invalid email format");
    }

    this.value = normalizedEmail;
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Email email = (Email) o;
    return Objects.equals(value, email.value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(value);
  }

  @Override
  public String toString() {
    return value;
  }
}
