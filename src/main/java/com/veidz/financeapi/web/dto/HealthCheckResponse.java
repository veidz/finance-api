package com.veidz.financeapi.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * Response DTO for health check endpoint.
 *
 * @author Veidz
 */
public record HealthCheckResponse(String status,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime timestamp, String version) {
  public static HealthCheckResponse up(String version) {
    return new HealthCheckResponse("UP", LocalDateTime.now(), version);
  }
}
