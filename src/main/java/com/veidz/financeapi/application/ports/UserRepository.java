package com.veidz.financeapi.application.ports;

import com.veidz.financeapi.domain.entities.User;
import com.veidz.financeapi.domain.valueobjects.Email;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for {@link User} entity.
 *
 * <p>This port defines the contract for persisting and retrieving users. Implementations should be
 * provided in the infrastructure layer.
 */
public interface UserRepository {

  /**
   * Saves a user to the repository.
   *
   * @param user the user to save
   * @return the saved user with generated ID if new
   */
  User save(User user);

  /**
   * Finds a user by their unique identifier.
   *
   * @param id the user's ID
   * @return an Optional containing the user if found, empty otherwise
   */
  Optional<User> findById(UUID id);

  /**
   * Finds a user by their email address.
   *
   * @param email the user's email
   * @return an Optional containing the user if found, empty otherwise
   */
  Optional<User> findByEmail(Email email);

  /**
   * Checks if a user with the given email exists.
   *
   * @param email the email to check
   * @return true if a user with this email exists, false otherwise
   */
  boolean existsByEmail(Email email);

  /**
   * Deletes a user by their unique identifier.
   *
   * @param id the user's ID
   */
  void deleteById(UUID id);
}
