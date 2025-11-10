package com.veidz.financeapi.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for development profile. Disables authentication for easier API testing
 * and Swagger access.
 *
 * @author Veidz
 */
@Configuration @EnableWebSecurity @Profile("dev")
public class SecurityConfig {

  /**
   * Configure security filter chain for development. Permits all requests without authentication.
   *
   * @param http HttpSecurity to configure
   * @return SecurityFilterChain configured
   * @throws Exception if configuration fails
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll()).csrf(csrf -> csrf.disable())
        .headers(headers -> headers.frameOptions(frame -> frame.disable()));

    return http.build();
  }
}
