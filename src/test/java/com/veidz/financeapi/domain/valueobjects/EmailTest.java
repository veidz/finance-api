package com.veidz.financeapi.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Email value object.
 * Following TDD: test first, then implement.
 *
 * @author Veidz
 */
class EmailTest {

  @Test
  void shouldCreateValidEmailWhenFormatIsCorrect() {
    // Arrange
    String validEmail = "user@example.com";

    // Act
    Email email = new Email(validEmail);

    // Assert
    assertNotNull(email);
    assertEquals(validEmail, email.getValue());
  }

  @Test
  void shouldRejectNullEmailWhenCreating() {
    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new Email(null)
    );
    assertEquals("Email cannot be null", exception.getMessage());
  }

  @Test
  void shouldRejectEmptyEmailWhenCreating() {
    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new Email("")
    );
    assertEquals("Email cannot be empty", exception.getMessage());
  }

  @Test
  void shouldRejectBlankEmailWhenCreating() {
    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new Email("   ")
    );
    assertEquals("Email cannot be empty", exception.getMessage());
  }

  @Test
  void shouldRejectInvalidEmailFormatWithoutAtSign() {
    // Arrange
    String invalidEmail = "userexample.com";

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new Email(invalidEmail)
    );
    assertEquals("Invalid email format", exception.getMessage());
  }

  @Test
  void shouldRejectInvalidEmailFormatWithoutDomain() {
    // Arrange
    String invalidEmail = "user@";

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new Email(invalidEmail)
    );
    assertEquals("Invalid email format", exception.getMessage());
  }

  @Test
  void shouldRejectInvalidEmailFormatWithoutLocalPart() {
    // Arrange
    String invalidEmail = "@example.com";

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new Email(invalidEmail)
    );
    assertEquals("Invalid email format", exception.getMessage());
  }

  @Test
  void shouldRejectInvalidEmailFormatWithSpaces() {
    // Arrange
    String invalidEmail = "user @example.com";

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new Email(invalidEmail)
    );
    assertEquals("Invalid email format", exception.getMessage());
  }

  @Test
  void shouldAcceptValidEmailWithSubdomain() {
    // Arrange
    String validEmail = "user@mail.example.com";

    // Act
    Email email = new Email(validEmail);

    // Assert
    assertNotNull(email);
    assertEquals(validEmail, email.getValue());
  }

  @Test
  void shouldAcceptValidEmailWithPlusSign() {
    // Arrange
    String validEmail = "user+tag@example.com";

    // Act
    Email email = new Email(validEmail);

    // Assert
    assertNotNull(email);
    assertEquals(validEmail, email.getValue());
  }

  @Test
  void shouldCompareEmailsCaseInsensitively() {
    // Arrange
    Email email1 = new Email("User@Example.COM");
    Email email2 = new Email("user@example.com");

    // Act & Assert
    assertTrue(email1.equals(email2));
    assertEquals(email1.hashCode(), email2.hashCode());
  }

  @Test
  void shouldConvertEmailToLowerCase() {
    // Arrange
    Email email = new Email("User@Example.COM");

    // Act & Assert
    assertEquals("user@example.com", email.getValue());
  }
}
