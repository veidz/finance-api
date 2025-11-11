package com.veidz.financeapi.domain.entities;

import static org.junit.jupiter.api.Assertions.*;

import com.veidz.financeapi.domain.enums.TransactionType;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Category Entity Tests")
class CategoryTest {

  // Test Data Builder
  private static class TestDataBuilder {
    private UUID userId = UUID.randomUUID();
    private String name = "Test Category";
    private TransactionType type = TransactionType.EXPENSE;
    private UUID parentCategoryId = null;

    TestDataBuilder withUserId(UUID userId) {
      this.userId = userId;
      return this;
    }

    TestDataBuilder withName(String name) {
      this.name = name;
      return this;
    }

    TestDataBuilder withType(TransactionType type) {
      this.type = type;
      return this;
    }

    TestDataBuilder withParentCategoryId(UUID parentCategoryId) {
      this.parentCategoryId = parentCategoryId;
      return this;
    }

    Category build() {
      if (parentCategoryId != null) {
        return Category.create(userId, name, type, parentCategoryId);
      }
      return Category.create(userId, name, type);
    }
  }

  private static TestDataBuilder aCategory() {
    return new TestDataBuilder();
  }

  @Nested
  @DisplayName("Creation Tests")
  class CreationTests {

    @Test
    @DisplayName("Should create category with required fields")
    void shouldCreateWithRequiredFields() {
      UUID userId = UUID.randomUUID();
      String name = "Salary";
      TransactionType type = TransactionType.INCOME;

      Category category = Category.create(userId, name, type);

      assertNotNull(category);
      assertNotNull(category.getId());
      assertEquals(userId, category.getUserId());
      assertEquals(name, category.getName());
      assertEquals(type, category.getType());
      assertNull(category.getParentCategoryId());
      assertNotNull(category.getCreatedAt());
    }

    @Test
    @DisplayName("Should create category with parent")
    void shouldCreateWithParent() {
      UUID parentCategoryId = UUID.randomUUID();
      Category category = aCategory().withParentCategoryId(parentCategoryId).build();

      assertEquals(parentCategoryId, category.getParentCategoryId());
    }



    @Test
    @DisplayName("Should throw exception when user ID is null")
    void shouldRejectNullUserId() {
      assertThrows(IllegalArgumentException.class, () -> aCategory().withUserId(null).build());
    }

    @Test
    @DisplayName("Should throw exception when name is null")
    void shouldRejectNullName() {
      assertThrows(IllegalArgumentException.class, () -> aCategory().withName(null).build());
    }

    @Test
    @DisplayName("Should throw exception when name is empty")
    void shouldRejectEmptyName() {
      assertThrows(IllegalArgumentException.class, () -> aCategory().withName("").build());
    }

    @Test
    @DisplayName("Should throw exception when name is blank")
    void shouldRejectBlankName() {
      assertThrows(IllegalArgumentException.class, () -> aCategory().withName("   ").build());
    }

    @Test
    @DisplayName("Should throw exception when type is null")
    void shouldRejectNullType() {
      assertThrows(IllegalArgumentException.class, () -> aCategory().withType(null).build());
    }
  }

  @Nested
  @DisplayName("Hierarchy Tests")
  class HierarchyTests {

    @Test
    @DisplayName("Should allow null parent category ID")
    void shouldAllowNullParent() {
      assertDoesNotThrow(() -> aCategory().withParentCategoryId(null).build());
    }
  }

  @Nested
  @DisplayName("Update Tests")
  class UpdateTests {

    @Test
    @DisplayName("Should update name successfully")
    void shouldUpdateName() {
      Category category = aCategory().withName("Old Name").build();

      category.updateName("New Name");

      assertEquals("New Name", category.getName());
    }

    @Test
    @DisplayName("Should throw exception when updating to null name")
    void shouldRejectNullInNameUpdate() {
      Category category = aCategory().build();

      assertThrows(IllegalArgumentException.class, () -> category.updateName(null));
    }

    @Test
    @DisplayName("Should throw exception when updating to empty name")
    void shouldRejectEmptyInNameUpdate() {
      Category category = aCategory().build();

      assertThrows(IllegalArgumentException.class, () -> category.updateName(""));
    }


  }
}
