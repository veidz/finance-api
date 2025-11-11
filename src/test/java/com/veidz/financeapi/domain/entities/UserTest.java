package com.veidz.financeapi.domain.entities;

import static org.junit.jupiter.api.Assertions.*;

import com.veidz.financeapi.domain.valueobjects.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("User Entity Tests")
class UserTest {

  // Test Data Builder
  private static class TestDataBuilder {
    private String name = "Test User";
    private Email email = new Email("test@example.com");
    private String password = "Password123";

    TestDataBuilder withName(String name) {
      this.name = name;
      return this;
    }

    TestDataBuilder withPassword(String password) {
      this.password = password;
      return this;
    }

    User build() {
      return User.create(name, email, password);
    }
  }

  private static TestDataBuilder aUser() {
    return new TestDataBuilder();
  }

  @Nested @DisplayName("Creation Tests")
  class CreationTests {

    @Test @DisplayName("Should create user with all valid data")
    void shouldCreateWithValidData() {
      String name = "John Doe";
      Email email = new Email("john.doe@example.com");
      String password = "SecurePass123";

      User user = User.create(name, email, password);

      assertNotNull(user);
      assertEquals(name, user.getName());
      assertEquals(email, user.getEmail());
      assertNotNull(user.getId());
      assertNotNull(user.getCreatedAt());
    }

    @Test @DisplayName("Should hash password when creating user")
    void shouldHashPassword() {
      String plainPassword = "MyPassword123";
      User user = aUser().withPassword(plainPassword).build();

      String hashedPassword = user.getPasswordHash();

      assertNotNull(hashedPassword);
      assertNotEquals(plainPassword, hashedPassword);
      assertTrue(hashedPassword.length() > plainPassword.length());
    }

    @Test @DisplayName("Should throw exception when name is null")
    void shouldRejectNullName() {
      assertThrows(IllegalArgumentException.class,
          () -> User.create(null, new Email("user@example.com"), "password123"));
    }

    @Test @DisplayName("Should throw exception when name is empty")
    void shouldRejectEmptyName() {
      assertThrows(IllegalArgumentException.class, () -> aUser().withName("").build());
    }

    @Test @DisplayName("Should throw exception when name is blank")
    void shouldRejectBlankName() {
      assertThrows(IllegalArgumentException.class, () -> aUser().withName("   ").build());
    }

    @Test @DisplayName("Should throw exception when email is null")
    void shouldRejectNullEmail() {
      assertThrows(IllegalArgumentException.class,
          () -> User.create("John Doe", null, "password123"));
    }

    @Test @DisplayName("Should throw exception when password is null")
    void shouldRejectNullPassword() {
      assertThrows(IllegalArgumentException.class,
          () -> User.create("John Doe", new Email("user@example.com"), null));
    }

    @Test @DisplayName("Should throw exception when password is empty")
    void shouldRejectEmptyPassword() {
      assertThrows(IllegalArgumentException.class, () -> aUser().withPassword("").build());
    }

    @Test @DisplayName("Should throw exception when password is blank")
    void shouldRejectBlankPassword() {
      assertThrows(IllegalArgumentException.class, () -> aUser().withPassword("   ").build());
    }
  }

  @Nested @DisplayName("Password Verification Tests")
  class PasswordVerificationTests {

    @Test @DisplayName("Should verify correct password")
    void shouldVerifyCorrectPassword() {
      String plainPassword = "MySecurePassword";
      User user = aUser().withPassword(plainPassword).build();

      assertTrue(user.verifyPassword(plainPassword));
    }

    @Test @DisplayName("Should reject incorrect password")
    void shouldRejectIncorrectPassword() {
      User user = aUser().withPassword("CorrectPassword").build();

      assertFalse(user.verifyPassword("WrongPassword"));
    }

    @Test @DisplayName("Should return false for null password in verification")
    void shouldReturnFalseForNullPassword() {
      User user = aUser().build();

      assertFalse(user.verifyPassword(null));
    }
  }

  @Nested @DisplayName("Update Tests")
  class UpdateTests {

    @Test @DisplayName("Should update name successfully")
    void shouldUpdateName() {
      User user = aUser().withName("Old Name").build();

      user.updateName("New Name");

      assertEquals("New Name", user.getName());
    }

    @Test @DisplayName("Should throw exception when updating to null name")
    void shouldRejectNullInNameUpdate() {
      User user = aUser().build();

      assertThrows(IllegalArgumentException.class, () -> user.updateName(null));
    }

    @Test @DisplayName("Should throw exception when updating to empty name")
    void shouldRejectEmptyInNameUpdate() {
      User user = aUser().build();

      assertThrows(IllegalArgumentException.class, () -> user.updateName(""));
    }
  }
}
