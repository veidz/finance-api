package com.veidz.financeapi.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.veidz.financeapi.application.dto.CreateUserRequest;
import com.veidz.financeapi.application.dto.UserResponse;
import com.veidz.financeapi.domain.entities.User;
import com.veidz.financeapi.domain.repositories.UserRepository;
import com.veidz.financeapi.domain.valueobjects.Email;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class) @DisplayName("CreateUserUseCase Tests")
class CreateUserUseCaseTest {

  @Mock
  private UserRepository userRepository;

  private CreateUserUseCase createUserUseCase;

  @BeforeEach
  void setUp() {
    createUserUseCase = new CreateUserUseCase(userRepository);
  }

  @Nested @DisplayName("Successful User Creation")
  class SuccessfulCreation {

    @Test @DisplayName("Should create user with hashed password")
    void shouldCreateUserWithHashedPassword() {
      // Given
      CreateUserRequest request = new CreateUserRequest("John Doe", "john@example.com",
          "password123");

      when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.empty());
      when(userRepository.save(any(User.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // When
      UserResponse response = createUserUseCase.execute(request);

      // Then
      assertNotNull(response);
      assertEquals("John Doe", response.name());
      assertEquals("john@example.com", response.email());
      assertNotNull(response.id());
      assertNotNull(response.createdAt());

      verify(userRepository).findByEmail(any(Email.class));
      verify(userRepository).save(any(User.class));
    }

    @Test @DisplayName("Should validate email format")
    void shouldValidateEmailFormat() {
      // Given
      CreateUserRequest request = new CreateUserRequest("John Doe", "john@example.com",
          "password123");

      when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.empty());
      when(userRepository.save(any(User.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));

      // When
      UserResponse response = createUserUseCase.execute(request);

      // Then
      assertNotNull(response);
      assertEquals("john@example.com", response.email());
    }
  }

  @Nested @DisplayName("Validation Errors")
  class ValidationErrors {

    @Test @DisplayName("Should throw exception if email already exists")
    void shouldThrowExceptionIfEmailAlreadyExists() {
      // Given
      CreateUserRequest request = new CreateUserRequest("John Doe", "john@example.com",
          "password123");
      User existingUser = User.create("Jane Doe", new Email("john@example.com"), "existingpass");

      when(userRepository.findByEmail(any(Email.class))).thenReturn(Optional.of(existingUser));

      // When & Then
      assertThrows(IllegalArgumentException.class, () -> createUserUseCase.execute(request));

      verify(userRepository).findByEmail(any(Email.class));
      verify(userRepository, never()).save(any(User.class));
    }

    @Test @DisplayName("Should throw exception for invalid email format")
    void shouldThrowExceptionForInvalidEmail() {
      // Given
      CreateUserRequest request = new CreateUserRequest("John Doe", "invalid-email", "password123");

      // When & Then
      assertThrows(IllegalArgumentException.class, () -> createUserUseCase.execute(request));

      verify(userRepository, never()).findByEmail(any(Email.class));
      verify(userRepository, never()).save(any(User.class));
    }

    @Test @DisplayName("Should throw exception for null name")
    void shouldThrowExceptionForNullName() {
      // Given
      CreateUserRequest request = new CreateUserRequest(null, "john@example.com", "password123");

      // When & Then
      assertThrows(IllegalArgumentException.class, () -> createUserUseCase.execute(request));

      verify(userRepository, never()).save(any(User.class));
    }

    @Test @DisplayName("Should throw exception for empty name")
    void shouldThrowExceptionForEmptyName() {
      // Given
      CreateUserRequest request = new CreateUserRequest("", "john@example.com", "password123");

      // When & Then
      assertThrows(IllegalArgumentException.class, () -> createUserUseCase.execute(request));

      verify(userRepository, never()).save(any(User.class));
    }

    @Test @DisplayName("Should throw exception for null password")
    void shouldThrowExceptionForNullPassword() {
      // Given
      CreateUserRequest request = new CreateUserRequest("John Doe", "john@example.com", null);

      // When & Then
      assertThrows(IllegalArgumentException.class, () -> createUserUseCase.execute(request));

      verify(userRepository, never()).save(any(User.class));
    }

    @Test @DisplayName("Should throw exception for empty password")
    void shouldThrowExceptionForEmptyPassword() {
      // Given
      CreateUserRequest request = new CreateUserRequest("John Doe", "john@example.com", "");

      // When & Then
      assertThrows(IllegalArgumentException.class, () -> createUserUseCase.execute(request));

      verify(userRepository, never()).save(any(User.class));
    }
  }
}
