package com.veidz.financeapi.web.controller;

import com.veidz.financeapi.web.dto.HealthCheckResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for health check endpoint.
 *
 * @author Veidz
 */
@RestController @RequestMapping("/api/health") @Tag(name = "Health Check", description = "Application health monitoring endpoints")
public class HealthCheckController {

  @Value("${spring.application.version:0.1.0}")
  private String version;

  /**
   * Get application health status.
   *
   * @return health check response with status, timestamp and version
   */
  @GetMapping @Operation(summary = "Check application health", description = "Returns the current health status of the application including version and timestamp")
  public HealthCheckResponse getHealth() {
    return HealthCheckResponse.up(version);
  }
}
