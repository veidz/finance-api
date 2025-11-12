package com.veidz.financeapi.domain.valueobjects;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DateRange value object.
 * Following TDD: test first, then implement.
 *
 * @author Veidz
 */
class DateRangeTest {

  @Test
  void shouldCreateValidDateRangeWhenStartIsBeforeEnd() {
    // Arrange
    LocalDate start = LocalDate.of(2025, 1, 1);
    LocalDate end = LocalDate.of(2025, 12, 31);

    // Act
    DateRange dateRange = new DateRange(start, end);

    // Assert
    assertNotNull(dateRange);
    assertEquals(start, dateRange.getStart());
    assertEquals(end, dateRange.getEnd());
  }

  @Test
  void shouldCreateValidDateRangeWhenStartEqualsEnd() {
    // Arrange
    LocalDate date = LocalDate.of(2025, 6, 15);

    // Act
    DateRange dateRange = new DateRange(date, date);

    // Assert
    assertNotNull(dateRange);
    assertEquals(date, dateRange.getStart());
    assertEquals(date, dateRange.getEnd());
  }

  @Test
  void shouldRejectNullStartDateWhenCreating() {
    // Arrange
    LocalDate end = LocalDate.of(2025, 12, 31);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new DateRange(null, end)
    );
    assertEquals("Start date cannot be null", exception.getMessage());
  }

  @Test
  void shouldRejectNullEndDateWhenCreating() {
    // Arrange
    LocalDate start = LocalDate.of(2025, 1, 1);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new DateRange(start, null)
    );
    assertEquals("End date cannot be null", exception.getMessage());
  }

  @Test
  void shouldRejectStartDateAfterEndDate() {
    // Arrange
    LocalDate start = LocalDate.of(2025, 12, 31);
    LocalDate end = LocalDate.of(2025, 1, 1);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new DateRange(start, end)
    );
    assertEquals("Start date must be before or equal to end date", exception.getMessage());
  }

  @Test
  void shouldCalculateDurationInDays() {
    // Arrange
    LocalDate start = LocalDate.of(2025, 1, 1);
    LocalDate end = LocalDate.of(2025, 1, 10);
    DateRange dateRange = new DateRange(start, end);

    // Act
    long durationInDays = dateRange.getDurationInDays();

    // Assert
    assertEquals(9, durationInDays);
  }

  @Test
  void shouldReturnZeroDurationWhenStartEqualsEnd() {
    // Arrange
    LocalDate date = LocalDate.of(2025, 1, 1);
    DateRange dateRange = new DateRange(date, date);

    // Act
    long durationInDays = dateRange.getDurationInDays();

    // Assert
    assertEquals(0, durationInDays);
  }

  @Test
  void shouldCheckIfDateIsWithinRange() {
    // Arrange
    LocalDate start = LocalDate.of(2025, 1, 1);
    LocalDate end = LocalDate.of(2025, 12, 31);
    DateRange dateRange = new DateRange(start, end);
    LocalDate dateInRange = LocalDate.of(2025, 6, 15);
    LocalDate dateBeforeRange = LocalDate.of(2024, 12, 31);
    LocalDate dateAfterRange = LocalDate.of(2026, 1, 1);

    // Act & Assert
    assertTrue(dateRange.contains(dateInRange));
    assertFalse(dateRange.contains(dateBeforeRange));
    assertFalse(dateRange.contains(dateAfterRange));
  }

  @Test
  void shouldIncludeBoundaryDatesWhenChecking() {
    // Arrange
    LocalDate start = LocalDate.of(2025, 1, 1);
    LocalDate end = LocalDate.of(2025, 12, 31);
    DateRange dateRange = new DateRange(start, end);

    // Act & Assert
    assertTrue(dateRange.contains(start));
    assertTrue(dateRange.contains(end));
  }

  @Test
  void shouldCheckIfRangesOverlap() {
    // Arrange
    DateRange range1 = new DateRange(
        LocalDate.of(2025, 1, 1),
        LocalDate.of(2025, 6, 30)
    );
    DateRange overlappingRange = new DateRange(
        LocalDate.of(2025, 6, 1),
        LocalDate.of(2025, 12, 31)
    );
    DateRange nonOverlappingRange = new DateRange(
        LocalDate.of(2026, 1, 1),
        LocalDate.of(2026, 12, 31)
    );

    // Act & Assert
    assertTrue(range1.overlaps(overlappingRange));
    assertFalse(range1.overlaps(nonOverlappingRange));
  }

  @Test
  void shouldCompareDateRangesCorrectly() {
    // Arrange
    DateRange range1 = new DateRange(
        LocalDate.of(2025, 1, 1),
        LocalDate.of(2025, 12, 31)
    );
    DateRange range2 = new DateRange(
        LocalDate.of(2025, 1, 1),
        LocalDate.of(2025, 12, 31)
    );
    DateRange range3 = new DateRange(
        LocalDate.of(2026, 1, 1),
        LocalDate.of(2026, 12, 31)
    );

    // Act & Assert
    assertTrue(range1.equals(range2));
    assertFalse(range1.equals(range3));
    assertEquals(range1.hashCode(), range2.hashCode());
  }
}
