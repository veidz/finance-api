package com.veidz.financeapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Finance API - Main Application Class.
 *
 * API REST para Controle Financeiro Pessoal com Assistente de Análise por IA. Desenvolvida seguindo
 * Clean Architecture, TDD e KISS principles.
 *
 * @author Veidz
 * @version 0.1.0
 */
@SpringBootApplication @OpenAPIDefinition(info = @Info(title = "Finance API", version = "0.1.0", description = "API REST para Controle Financeiro Pessoal com Assistente de Análise por IA", contact = @Contact(name = "Veidz", url = "https://github.com/veidz/finance-api")))
public class FinanceApiApplication {
  public static void main(String[] args) {
    SpringApplication.run(FinanceApiApplication.class, args);
  }
}
