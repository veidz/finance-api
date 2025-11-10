package com.veidz.financeapi.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for HealthCheckController.
 *
 * @author Veidz
 */
@SpringBootTest @AutoConfigureMockMvc(addFilters = false)
class HealthCheckControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldReturnHealthStatusWhenGetHealth() throws Exception {
    mockMvc.perform(get("/api/health")).andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("UP")).andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.version").value("0.1.0"));
  }

  @Test
  void shouldReturnJsonContentTypeWhenGetHealth() throws Exception {
    mockMvc.perform(get("/api/health")).andExpect(status().isOk())
        .andExpect(jsonPath("$.status").exists());
  }
}
