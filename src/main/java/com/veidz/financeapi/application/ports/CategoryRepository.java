package com.veidz.financeapi.application.ports;

import com.veidz.financeapi.domain.entities.Category;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for {@link Category} entity.
 *
 * <p>
 * This port defines the contract for persisting and retrieving categories. Implementations should
 * be provided in the infrastructure layer.
 */
public interface CategoryRepository {

  /**
   * Saves a category to the repository.
   *
   * @param category the category to save
   * @return the saved category with generated ID if new
   */
  Category save(Category category);

  /**
   * Finds a category by its unique identifier.
   *
   * @param id the category's ID
   * @return an Optional containing the category if found, empty otherwise
   */
  Optional<Category> findById(UUID id);

  /**
   * Deletes a category by its unique identifier.
   *
   * @param id the category's ID
   */
  void deleteById(UUID id);
}
