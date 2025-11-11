package com.veidz.financeapi.domain.entities;

import com.veidz.financeapi.domain.enums.TransactionType;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Category domain entity representing a transaction category in the system. Contains business logic
 * and invariants for category management, including support for hierarchical categories
 * (parent-child relationships).
 *
 * @author Veidz
 */
public final class Category {

  private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^#[0-9A-Fa-f]{6}$");

  private final UUID id;
  private final UUID userId;
  private String name;
  private final TransactionType type;
  private UUID parentCategoryId;
  private String color;
  private final LocalDateTime createdAt;

  /**
   * Private constructor to prevent direct instantiation. Use static factory method instead.
   */
  private Category(UUID id, UUID userId, String name, TransactionType type, UUID parentCategoryId,
      LocalDateTime createdAt) {
    this.id = id;
    this.userId = userId;
    this.name = name;
    this.type = type;
    this.parentCategoryId = parentCategoryId;
    this.createdAt = createdAt;
  }

  /**
   * Creates a new Category with the given details (without parent category).
   *
   * @param userId the user ID who owns this category (must be non-null)
   * @param name the category name (must be non-null and non-empty)
   * @param type the transaction type for this category (must be non-null)
   * @return a new Category instance
   * @throws IllegalArgumentException if any validation fails
   */
  public static Category create(UUID userId, String name, TransactionType type) {
    return create(userId, name, type, null);
  }

  /**
   * Creates a new Category with the given details (with optional parent category).
   *
   * @param userId the user ID who owns this category (must be non-null)
   * @param name the category name (must be non-null and non-empty)
   * @param type the transaction type for this category (must be non-null)
   * @param parentCategoryId the parent category ID for hierarchical categories (can be null)
   * @return a new Category instance
   * @throws IllegalArgumentException if any validation fails
   */
  public static Category create(UUID userId, String name, TransactionType type,
      UUID parentCategoryId) {
    validateUserId(userId);
    validateName(name);
    validateType(type);

    return new Category(UUID.randomUUID(), userId, name.trim(), type, parentCategoryId,
        LocalDateTime.now());
  }

  /**
   * Updates the category name.
   *
   * @param newName the new name (must be non-null and non-empty)
   * @throws IllegalArgumentException if name is invalid
   */
  public void updateName(String newName) {
    validateName(newName);
    this.name = newName.trim();
  }

  /**
   * Sets the parent category ID for creating hierarchical categories.
   *
   * @param parentCategoryId the parent category ID (can be null)
   */
  public void setParentCategoryId(UUID parentCategoryId) {
    this.parentCategoryId = parentCategoryId;
  }

  /**
   * Sets the color for this category using hex format.
   *
   * @param color the color in hex format (e.g., "#FF5733"), can be null
   * @throws IllegalArgumentException if color format is invalid
   */
  public void setColor(String color) {
    if (color != null) {
      validateColor(color);
    }
    this.color = color;
  }

  /**
   * Checks if this category is a subcategory (has a parent category).
   *
   * @return true if this category has a parent, false otherwise
   */
  public boolean isSubcategory() {
    return parentCategoryId != null;
  }

  private static void validateUserId(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("User ID cannot be null");
    }
  }

  private static void validateName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Category name cannot be null");
    }
    if (name.trim().isEmpty()) {
      throw new IllegalArgumentException("Category name cannot be empty");
    }
  }

  private static void validateType(TransactionType type) {
    if (type == null) {
      throw new IllegalArgumentException("Transaction type cannot be null");
    }
  }

  private static void validateColor(String color) {
    if (color.trim().isEmpty()) {
      throw new IllegalArgumentException("Color cannot be empty");
    }
    if (!HEX_COLOR_PATTERN.matcher(color).matches()) {
      throw new IllegalArgumentException("Color must be in hex format (e.g., #FF5733)");
    }
  }

  public UUID getId() {
    return id;
  }

  public UUID getUserId() {
    return userId;
  }

  public String getName() {
    return name;
  }

  public TransactionType getType() {
    return type;
  }

  public UUID getParentCategoryId() {
    return parentCategoryId;
  }

  public String getColor() {
    return color;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Category category = (Category) obj;
    return Objects.equals(id, category.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Category{" + "id=" + id + ", userId=" + userId + ", name='" + name + '\'' + ", type="
        + type + ", parentCategoryId=" + parentCategoryId + ", color='" + color + '\''
        + ", createdAt=" + createdAt + '}';
  }
}
