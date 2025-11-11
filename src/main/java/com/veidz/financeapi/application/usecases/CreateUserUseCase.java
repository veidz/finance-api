package com.veidz.financeapi.application.usecases;

import com.veidz.financeapi.application.dto.CreateUserRequest;
import com.veidz.financeapi.application.dto.UserResponse;
import com.veidz.financeapi.domain.entities.User;
import com.veidz.financeapi.domain.repositories.UserRepository;
import com.veidz.financeapi.domain.valueobjects.Email;

/**
 * Use case for creating a new user. Follows Single Responsibility Principle: only creates users.
 *
 * @author Veidz
 */
public class CreateUserUseCase {

  private final UserRepository userRepository;

  public CreateUserUseCase(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Executes the use case to create a new user.
   *
   * @param request the create user request containing name, email, and password
   * @return UserResponse with the created user's data
   * @throws IllegalArgumentException if validation fails or email already exists
   */
  public UserResponse execute(CreateUserRequest request) {
    // Validate input
    validateRequest(request);

    // Create Email value object (will validate format)
    Email email = new Email(request.email());

    // Check if user already exists
    if (userRepository.findByEmail(email).isPresent()) {
      throw new IllegalArgumentException("User with email " + request.email() + " already exists");
    }

    // Create user entity (will hash password)
    User user = User.create(request.name(), email, request.password());

    // Save user
    User savedUser = userRepository.save(user);

    // Map to response DTO
    return new UserResponse(
        savedUser.getId(), savedUser.getName(), savedUser.getEmail().getValue(), savedUser.getCreatedAt());
  }

  private void validateRequest(CreateUserRequest request) {
    if (request.name() == null || request.name().trim().isEmpty()) {
      throw new IllegalArgumentException("Name cannot be null or empty");
    }
    if (request.password() == null || request.password().trim().isEmpty()) {
      throw new IllegalArgumentException("Password cannot be null or empty");
    }
  }
}
