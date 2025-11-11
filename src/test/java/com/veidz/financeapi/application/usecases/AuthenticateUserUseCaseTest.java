package com.veidz.financeapi.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.veidz.financeapi.application.dto.AuthenticationRequest;
import com.veidz.financeapi.application.dto.AuthenticationResponse;
import com.veidz.financeapi.application.ports.UserRepository;
import com.veidz.financeapi.domain.entities.User;
import com.veidz.financeapi.domain.valueobjects.Email;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticateUserUseCase Tests")
class AuthenticateUserUseCaseTest {

  @Mock private UserRepository userRepository;

  private AuthenticateUserUseCase authenticateUserUseCase;

  @BeforeEach
  void setUp() {
    authenticateUserUseCase = new AuthenticateUserUseCase(userRepository);
  }

  @Nested
  @DisplayName("Successful Authentication")
  class SuccessfulAuthentication {

    @Test
    @DisplayName("should authenticate user with correct credentials")
    void shouldAuthenticateWithCorrectCredentials() {
      // Given
      String email = "john@example.com";
      String password = "SecurePass123";
      User user = User.create("John Doe", new Email(email), password);

      when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(user));

      AuthenticationRequest request = new AuthenticationRequest(email, password);

      // When
      AuthenticationResponse response = authenticateUserUseCase.execute(request);

      // Then
      assertNotNull(response);
      assertEquals(user.getId(), response.userId());
      assertEquals("John Doe", response.name());
      assertEquals(email, response.email());
      assertNotNull(response.token()); // Token should be generated
      verify(userRepository).findByEmail(any(Email.class));
    }

    @Test
    @DisplayName("should verify password correctly")
    void shouldVerifyPasswordCorrectly() {
      // Given
      String email = "jane@example.com";
      String password = "MyPassword456";
      User user = User.create("Jane Smith", new Email(email), password);

      when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(user));

      AuthenticationRequest request = new AuthenticationRequest(email, password);

      // When
      AuthenticationResponse response = authenticateUserUseCase.execute(request);

      // Then
      assertNotNull(response);
      assertTrue(user.verifyPassword(password));
    }
  }

  @Nested
  @DisplayName("Authentication Failures")
  class AuthenticationFailures {

    @Test
    @DisplayName("should throw exception when password is incorrect")
    void shouldThrowExceptionForWrongPassword() {
      // Given
      String email = "john@example.com";
      String correctPassword = "CorrectPass123";
      String wrongPassword = "WrongPass123";
      User user = User.create("John Doe", new Email(email), correctPassword);

      when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(user));

      AuthenticationRequest request = new AuthenticationRequest(email, wrongPassword);

      // When & Then
      IllegalArgumentException exception =
          assertThrows(
              IllegalArgumentException.class, () -> authenticateUserUseCase.execute(request));

      assertEquals("Invalid credentials", exception.getMessage());
      verify(userRepository).findByEmail(any(Email.class));
    }

    @Test
    @DisplayName("should throw exception when email does not exist")
    void shouldThrowExceptionForNonExistentEmail() {
      // Given
      String email = "nonexistent@example.com";
      String password = "SomePassword123";

      when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.empty());

      AuthenticationRequest request = new AuthenticationRequest(email, password);

      // When & Then
      IllegalArgumentException exception =
          assertThrows(
              IllegalArgumentException.class, () -> authenticateUserUseCase.execute(request));

      assertEquals("Invalid credentials", exception.getMessage());
      verify(userRepository).findByEmail(any(Email.class));
    }

    @Test
    @DisplayName("should throw exception when email is null")
    void shouldThrowExceptionForNullEmail() {
      // Given
      AuthenticationRequest request = new AuthenticationRequest(null, "password");

      // When & Then
      IllegalArgumentException exception =
          assertThrows(
              IllegalArgumentException.class, () -> authenticateUserUseCase.execute(request));

      assertEquals("Email and password are required", exception.getMessage());
      verify(userRepository, never()).findByEmail(any(Email.class));
    }

    @Test
    @DisplayName("should throw exception when email is empty")
    void shouldThrowExceptionForEmptyEmail() {
      // Given
      AuthenticationRequest request = new AuthenticationRequest("", "password");

      // When & Then
      IllegalArgumentException exception =
          assertThrows(
              IllegalArgumentException.class, () -> authenticateUserUseCase.execute(request));

      assertEquals("Email and password are required", exception.getMessage());
      verify(userRepository, never()).findByEmail(any(Email.class));
    }

    @Test
    @DisplayName("should throw exception when password is null")
    void shouldThrowExceptionForNullPassword() {
      // Given
      AuthenticationRequest request = new AuthenticationRequest("john@example.com", null);

      // When & Then
      IllegalArgumentException exception =
          assertThrows(
              IllegalArgumentException.class, () -> authenticateUserUseCase.execute(request));

      assertEquals("Email and password are required", exception.getMessage());
      verify(userRepository, never()).findByEmail(any(Email.class));
    }

    @Test
    @DisplayName("should throw exception when password is empty")
    void shouldThrowExceptionForEmptyPassword() {
      // Given
      AuthenticationRequest request = new AuthenticationRequest("john@example.com", "");

      // When & Then
      IllegalArgumentException exception =
          assertThrows(
              IllegalArgumentException.class, () -> authenticateUserUseCase.execute(request));

      assertEquals("Email and password are required", exception.getMessage());
      verify(userRepository, never()).findByEmail(any(Email.class));
    }
  }
}
