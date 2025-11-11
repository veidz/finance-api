package com.veidz.financeapi.domain.entities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.veidz.financeapi.domain.enums.TransactionType;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Category entity.
 *
 * @author Veidz
 */
class CategoryTest {

  @Test
  void shouldCreateCategoryWithRequiredFields() {
    // Given
    UUID userId = UUID.randomUUID();
    String name = "Salary";
    TransactionType type = TransactionType.INCOME;

    // When
    Category category = Category.create(userId, name, type);

    // Then
    assertNotNull(category);
    assertNotNull(category.getId());
    assertEquals(userId, category.getUserId());
    assertEquals(name, category.getName());
    assertEquals(type, category.getType());
    assertNull(category.getParentCategoryId());
    assertNull(category.getColor());
    assertNotNull(category.getCreatedAt());
  }

  @Test
  void shouldCreateCategoryWithParent() {
    // Given
    UUID userId = UUID.randomUUID();
    UUID parentCategoryId = UUID.randomUUID();
    String name = "Monthly Salary";
    TransactionType type = TransactionType.INCOME;

    // When
    Category category = Category.create(userId, name, type, parentCategoryId);

    // Then
    assertEquals(parentCategoryId, category.getParentCategoryId());
  }

  @Test
  void shouldCreateCategoryWithColor() {
    // Given
    UUID userId = UUID.randomUUID();
    String name = "Food";
    TransactionType type = TransactionType.EXPENSE;
    String color = "#FF5733";

    // When
    Category category = Category.create(userId, name, type);
    category.setColor(color);

    // Then
    assertEquals(color, category.getColor());
  }

  @Test
  void shouldThrowExceptionWhenUserIdIsNull() {
    // Given
    String name = "Test Category";
    TransactionType type = TransactionType.INCOME;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> Category.create(null, name, type));
  }

  @Test
  void shouldThrowExceptionWhenNameIsNull() {
    // Given
    UUID userId = UUID.randomUUID();
    TransactionType type = TransactionType.INCOME;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> Category.create(userId, null, type));
  }

  @Test
  void shouldThrowExceptionWhenNameIsEmpty() {
    // Given
    UUID userId = UUID.randomUUID();
    TransactionType type = TransactionType.INCOME;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> Category.create(userId, "", type));
  }

  @Test
  void shouldThrowExceptionWhenNameIsBlank() {
    // Given
    UUID userId = UUID.randomUUID();
    TransactionType type = TransactionType.INCOME;

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> Category.create(userId, "   ", type));
  }

  @Test
  void shouldThrowExceptionWhenTypeIsNull() {
    // Given
    UUID userId = UUID.randomUUID();
    String name = "Test Category";

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> Category.create(userId, name, null));
  }

  @Test
  void shouldTrimCategoryName() {
    // Given
    UUID userId = UUID.randomUUID();
    String name = "  Groceries  ";
    TransactionType type = TransactionType.EXPENSE;

    // When
    Category category = Category.create(userId, name, type);

    // Then
    assertEquals("Groceries", category.getName());
  }

  @Test
  void shouldUpdateCategoryName() {
    // Given
    UUID userId = UUID.randomUUID();
    Category category = Category.create(userId, "Old Name", TransactionType.EXPENSE);

    // When
    category.updateName("New Name");

    // Then
    assertEquals("New Name", category.getName());
  }

  @Test
  void shouldThrowExceptionWhenUpdatingNameToNull() {
    // Given
    UUID userId = UUID.randomUUID();
    Category category = Category.create(userId, "Old Name", TransactionType.EXPENSE);

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> category.updateName(null));
  }

  @Test
  void shouldThrowExceptionWhenUpdatingNameToEmpty() {
    // Given
    UUID userId = UUID.randomUUID();
    Category category = Category.create(userId, "Old Name", TransactionType.EXPENSE);

    // When & Then
    assertThrows(IllegalArgumentException.class, () -> category.updateName(""));
  }

  @Test
  void shouldAllowSettingParentCategory() {
    // Given
    UUID userId = UUID.randomUUID();
    UUID parentCategoryId = UUID.randomUUID();
    Category category = Category.create(userId, "Subcategory", TransactionType.EXPENSE);

    // When
    category.setParentCategoryId(parentCategoryId);

    // Then
    assertEquals(parentCategoryId, category.getParentCategoryId());
  }

  @Test
  void shouldAllowNullParentCategory() {
    // Given
    UUID userId = UUID.randomUUID();
    UUID parentCategoryId = UUID.randomUUID();
    Category category = Category.create(userId, "Test", TransactionType.EXPENSE, parentCategoryId);

    // When
    category.setParentCategoryId(null);

    // Then
    assertNull(category.getParentCategoryId());
  }

  @Test
  void shouldCheckIfCategoryIsSubcategory() {
    // Given
    UUID userId = UUID.randomUUID();
    UUID parentCategoryId = UUID.randomUUID();

    Category parentCategory = Category.create(userId, "Parent", TransactionType.EXPENSE);
    Category subcategory =
        Category.create(userId, "Subcategory", TransactionType.EXPENSE, parentCategoryId);

    // Then
    assertTrue(subcategory.isSubcategory());
    assertEquals(false, parentCategory.isSubcategory());
  }

  @Test
  void shouldValidateColorFormat() {
    // Given
    UUID userId = UUID.randomUUID();
    Category category = Category.create(userId, "Test", TransactionType.EXPENSE);

    // When & Then - Valid hex colors
    assertDoesNotThrow(() -> category.setColor("#FF5733"));
    assertDoesNotThrow(() -> category.setColor("#000000"));
    assertDoesNotThrow(() -> category.setColor("#FFFFFF"));
    assertDoesNotThrow(() -> category.setColor(null)); // null is allowed
  }

  @Test
  void shouldThrowExceptionForInvalidColorFormat() {
    // Given
    UUID userId = UUID.randomUUID();
    Category category = Category.create(userId, "Test", TransactionType.EXPENSE);

    // When & Then - Invalid colors
    assertThrows(IllegalArgumentException.class, () -> category.setColor("FF5733")); // Missing #
    assertThrows(IllegalArgumentException.class, () -> category.setColor("#FF57")); // Too short
    assertThrows(IllegalArgumentException.class, () -> category.setColor("#FF57339")); // Too long
    assertThrows(IllegalArgumentException.class, () -> category.setColor("#GGGGGG")); // Invalid hex
    assertThrows(IllegalArgumentException.class, () -> category.setColor("")); // Empty
  }

  @Test
  void shouldAllowCategoryForIncomeType() {
    // Given
    UUID userId = UUID.randomUUID();

    // When
    Category category = Category.create(userId, "Salary", TransactionType.INCOME);

    // Then
    assertEquals(TransactionType.INCOME, category.getType());
  }

  @Test
  void shouldAllowCategoryForExpenseType() {
    // Given
    UUID userId = UUID.randomUUID();

    // When
    Category category = Category.create(userId, "Food", TransactionType.EXPENSE);

    // Then
    assertEquals(TransactionType.EXPENSE, category.getType());
  }
}
