package com.veidz.financeapi.domain.valueobjects;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Value Object representing a date range with start and end dates.
 * Immutable and ensures business invariants.
 *
 * @author Veidz
 */
public final class DateRange {

  private final LocalDate start;
  private final LocalDate end;

  /**
   * Creates a DateRange instance with the given start and end dates.
   *
   * @param start the start date (must be non-null and before or equal to end date)
   * @param end the end date (must be non-null)
   * @throws IllegalArgumentException if start or end is null, or if start is after end
   */
  public DateRange(LocalDate start, LocalDate end) {
    if (start == null) {
      throw new IllegalArgumentException("Start date cannot be null");
    }
    if (end == null) {
      throw new IllegalArgumentException("End date cannot be null");
    }
    if (start.isAfter(end)) {
      throw new IllegalArgumentException("Start date must be before or equal to end date");
    }
    this.start = start;
    this.end = end;
  }

  /**
   * Returns the duration of the date range in days.
   *
   * @return the number of days between start and end (exclusive of end)
   */
  public long getDurationInDays() {
    return ChronoUnit.DAYS.between(start, end);
  }

  /**
   * Checks if the given date is within this date range (inclusive).
   *
   * @param date the date to check
   * @return true if the date is within the range (including boundaries)
   */
  public boolean contains(LocalDate date) {
    return !date.isBefore(start) && !date.isAfter(end);
  }

  /**
   * Checks if this date range overlaps with another date range.
   *
   * @param other the other date range
   * @return true if the ranges overlap
   */
  public boolean overlaps(DateRange other) {
    return !this.end.isBefore(other.start) && !other.end.isBefore(this.start);
  }

  public LocalDate getStart() {
    return start;
  }

  public LocalDate getEnd() {
    return end;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DateRange dateRange = (DateRange) o;
    return Objects.equals(start, dateRange.start) && Objects.equals(end, dateRange.end);
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end);
  }

  @Override
  public String toString() {
    return start + " to " + end;
  }
}
