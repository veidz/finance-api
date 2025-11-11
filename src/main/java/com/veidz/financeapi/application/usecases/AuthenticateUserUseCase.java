package com.veidz.financeapi.application.usecases;

import com.veidz.financeapi.application.dto.AuthenticationRequest;
import com.veidz.financeapi.application.dto.AuthenticationResponse;
import com.veidz.financeapi.application.ports.UserRepository;
import com.veidz.financeapi.domain.entities.User;
import com.veidz.financeapi.domain.valueobjects.Email;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Optional;

/**
 * Use case for authenticating a user.
 *
 * <p>
 * This use case handles user authentication by verifying credentials and generating an
 * authentication token.
 */
public class AuthenticateUserUseCase {

  private final UserRepository userRepository;

  @SuppressFBWarnings(
      value = {"EI_EXPOSE_REP2", "CT_CONSTRUCTOR_THROW"},
      justification =
          "UserRepository is an interface injected by DI container. "
              + "Constructor validation is required for fail-fast behavior.")
  public AuthenticateUserUseCase(UserRepository userRepository) {
    if (userRepository == null) {
      throw new IllegalArgumentException("UserRepository cannot be null");
    }
    this.userRepository = userRepository;
  }

  /**
   * Executes the authentication use case.
   *
   * @param request the authentication request containing email and password
   * @return the authentication response with user details and token
   * @throws IllegalArgumentException if credentials are invalid
   */
  public AuthenticationResponse execute(AuthenticationRequest request) {
    validateRequest(request);

    Email email = new Email(request.email());
    Optional<User> userOptional = userRepository.findByEmail(email);

    if (userOptional.isEmpty()) {
      throw new IllegalArgumentException("Invalid credentials");
    }

    User user = userOptional.get();

    if (!user.verifyPassword(request.password())) {
      throw new IllegalArgumentException("Invalid credentials");
    }

    // TODO: Generate JWT token when JWT infrastructure is implemented
    String token = "temporary-token-" + user.getId();

    return new AuthenticationResponse(
        user.getId(), user.getName(), user.getEmail().getValue(), token);
  }

  private void validateRequest(AuthenticationRequest request) {
    if (request.email() == null
        || request.email().isBlank()
        || request.password() == null
        || request.password().isBlank()) {
      throw new IllegalArgumentException("Email and password are required");
    }
  }
}
