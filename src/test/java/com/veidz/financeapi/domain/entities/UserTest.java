package com.veidz.financeapi.domain.entities;

import com.veidz.financeapi.domain.valueobjects.Email;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for User entity.
 * Following TDD: test first, then implement.
 *
 * @author Veidz
 */
class UserTest {

  @Test
  void shouldCreateUserWhenAllDataIsValid() {
    // Arrange
    String name = "John Doe";
    Email email = new Email("john.doe@example.com");
    String password = "SecurePass123";

    // Act
    User user = new User(name, email, password);

    // Assert
    assertNotNull(user);
    assertEquals(name, user.getName());
    assertEquals(email, user.getEmail());
    assertNotNull(user.getId());
    assertNotNull(user.getCreatedAt());
  }

  @Test
  void shouldHashPasswordWhenCreatingUser() {
    // Arrange
    String plainPassword = "MyPassword123";
    User user = new User("Jane Doe", new Email("jane@example.com"), plainPassword);

    // Act
    String hashedPassword = user.getPasswordHash();

    // Assert
    assertNotNull(hashedPassword);
    assertNotEquals(plainPassword, hashedPassword);
    assertTrue(hashedPassword.length() > plainPassword.length());
  }

  @Test
  void shouldRejectNullNameWhenCreatingUser() {
    // Arrange
    Email email = new Email("user@example.com");
    String password = "password123";

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new User(null, email, password)
    );
    assertEquals("Name cannot be null or empty", exception.getMessage());
  }

  @Test
  void shouldRejectEmptyNameWhenCreatingUser() {
    // Arrange
    Email email = new Email("user@example.com");
    String password = "password123";

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new User("", email, password)
    );
    assertEquals("Name cannot be null or empty", exception.getMessage());
  }

  @Test
  void shouldRejectBlankNameWhenCreatingUser() {
    // Arrange
    Email email = new Email("user@example.com");
    String password = "password123";

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new User("   ", email, password)
    );
    assertEquals("Name cannot be null or empty", exception.getMessage());
  }

  @Test
  void shouldRejectNullEmailWhenCreatingUser() {
    // Arrange
    String name = "John Doe";
    String password = "password123";

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new User(name, null, password)
    );
    assertEquals("Email cannot be null", exception.getMessage());
  }

  @Test
  void shouldRejectNullPasswordWhenCreatingUser() {
    // Arrange
    String name = "John Doe";
    Email email = new Email("john@example.com");

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new User(name, email, null)
    );
    assertEquals("Password cannot be null or empty", exception.getMessage());
  }

  @Test
  void shouldRejectEmptyPasswordWhenCreatingUser() {
    // Arrange
    String name = "John Doe";
    Email email = new Email("john@example.com");

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new User(name, email, "")
    );
    assertEquals("Password cannot be null or empty", exception.getMessage());
  }

  @Test
  void shouldRejectShortPasswordWhenCreatingUser() {
    // Arrange
    String name = "John Doe";
    Email email = new Email("john@example.com");
    String shortPassword = "123";

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new User(name, email, shortPassword)
    );
    assertEquals("Password must be at least 6 characters long", exception.getMessage());
  }

  @Test
  void shouldVerifyPasswordCorrectlyWhenPasswordMatches() {
    // Arrange
    String plainPassword = "MySecurePass123";
    User user = new User("John Doe", new Email("john@example.com"), plainPassword);

    // Act
    boolean isValid = user.verifyPassword(plainPassword);

    // Assert
    assertTrue(isValid);
  }

  @Test
  void shouldRejectPasswordWhenPasswordDoesNotMatch() {
    // Arrange
    String plainPassword = "MySecurePass123";
    User user = new User("John Doe", new Email("john@example.com"), plainPassword);

    // Act
    boolean isValid = user.verifyPassword("WrongPassword");

    // Assert
    assertFalse(isValid);
  }

  @Test
  void shouldUpdateNameWhenNewNameIsValid() {
    // Arrange
    User user = new User("Old Name", new Email("user@example.com"), "password123");
    String newName = "New Name";

    // Act
    user.updateName(newName);

    // Assert
    assertEquals(newName, user.getName());
  }

  @Test
  void shouldRejectUpdatingNameWhenNewNameIsInvalid() {
    // Arrange
    User user = new User("Old Name", new Email("user@example.com"), "password123");

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> user.updateName("")
    );
    assertEquals("Name cannot be null or empty", exception.getMessage());
  }

  @Test
  void shouldChangePasswordWhenCurrentPasswordIsCorrect() {
    // Arrange
    String currentPassword = "OldPassword123";
    User user = new User("John Doe", new Email("john@example.com"), currentPassword);
    String newPassword = "NewPassword456";

    // Act
    user.changePassword(currentPassword, newPassword);

    // Assert
    assertTrue(user.verifyPassword(newPassword));
    assertFalse(user.verifyPassword(currentPassword));
  }

  @Test
  void shouldRejectPasswordChangeWhenCurrentPasswordIsWrong() {
    // Arrange
    String currentPassword = "OldPassword123";
    User user = new User("John Doe", new Email("john@example.com"), currentPassword);
    String wrongPassword = "WrongPassword";
    String newPassword = "NewPassword456";

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> user.changePassword(wrongPassword, newPassword)
    );
    assertEquals("Current password is incorrect", exception.getMessage());
  }

  @Test
  void shouldGenerateUniqueIdsForDifferentUsers() {
    // Arrange & Act
    User user1 = new User("User One", new Email("user1@example.com"), "password123");
    User user2 = new User("User Two", new Email("user2@example.com"), "password123");

    // Assert
    assertNotEquals(user1.getId(), user2.getId());
  }

  @Test
  void shouldCompareTwoUsersById() {
    // Arrange
    User user1 = new User("User One", new Email("user1@example.com"), "password123");
    User user2 = new User("User Two", new Email("user2@example.com"), "password123");

    // Act & Assert
    assertNotEquals(user1, user2);
    assertEquals(user1, user1);
  }
}
