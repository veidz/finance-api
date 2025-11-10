package com.veidz.financeapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Finance API - Main Application Class.
 *
 * API REST para Controle Financeiro Pessoal com Assistente de Análise por IA. Desenvolvida seguindo
 * Clean Architecture, TDD e KISS principles.
 *
 * @author Veidz
 * @version 0.1.0
 */
@SpringBootApplication @EnableJpaAuditing
public class FinanceApiApplication {
  public static void main(String[] args) {
    SpringApplication.run(FinanceApiApplication.class, args);
  }
}
