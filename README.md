# 💰 Finance API - Controle Financeiro com IA

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)
![CI](https://img.shields.io/badge/CI-GitHub%20Actions-success)

API REST para controle financeiro pessoal com assistente de análise por inteligência artificial.

## 🎯 Sobre o Projeto

Sistema completo de gestão financeira que permite:

- 📊 Cadastro de transações (receitas e despesas)
- 🏷️ Categorização de gastos
- 💵 Definição de orçamentos com alertas
- 🎯 Metas financeiras personalizadas
- 📈 Relatórios detalhados
- 🤖 **Análise inteligente via IA** com sugestões de economia, previsões de gastos e recomendações personalizadas

## 🏗️ Arquitetura

Este projeto segue **Clean Architecture** (Arquitetura Hexagonal) com separação clara de responsabilidades:

```
📦 Domain Layer (Entities, Value Objects, Business Rules)
    ⬇️
📦 Application Layer (Use Cases)
    ⬇️
📦 Infrastructure Layer (Persistence, Security, AI Integration)
    ⬇️
📦 Web Layer (REST Controllers, DTOs)
```

**Princípios aplicados**:

- ✅ **TDD** (Test-Driven Development)
- ✅ **Clean Code**
- ✅ **SOLID**
- ✅ **KISS** (Keep It Simple, Stupid)
- ✅ **Design Patterns** (Repository, Factory, Strategy, Adapter, Builder)

## 🚀 Tecnologias

- **Backend**: Java 17, Spring Boot 3.2+
- **Database**: PostgreSQL (Supabase)
- **Security**: Spring Security + JWT
- **IA**: OpenAI API / Groq / Google Gemini
- **Testing**: JUnit 5, Mockito, Testcontainers, REST Assured
- **Quality**: JaCoCo, Spotbugs, Checkstyle
- **DevOps**: Docker, Docker Compose, GitHub Actions
- **Deploy**: Railway / Render
- **Documentation**: SpringDoc OpenAPI (Swagger)

## 📚 Documentação

- **[Arquitetura Completa](ARCHITECTURE.md)** - Detalhes da arquitetura, estrutura de pastas, stack tecnológica
- **[Plano de Execução](EXECUTION_PLAN.md)** - Roadmap detalhado de implementação (8 semanas)
- **API Docs**: `/swagger-ui.html` (disponível após iniciar a aplicação)

## 🛠️ Setup Local

### Pré-requisitos

- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- VS Code (recomendado) + extensões Java

### Instalação

1. **Clone o repositório**:

```bash
git clone https://github.com/veidz/finance-api.git
cd finance-api
```

2. **Configure variáveis de ambiente**:

```bash
cp .env.example .env
# Edite .env com suas credenciais
```

3. **Inicie o ambiente com Docker**:

```bash
docker-compose up -d
```

4. **Execute a aplicação**:

```bash
./mvnw spring-boot:run
```

5. **Acesse**:

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Actuator: http://localhost:8080/actuator/health

### Executar Testes

```bash
# Unit tests
./mvnw test

# Integration tests
./mvnw verify -P integration-tests

# Coverage report
./mvnw test jacoco:report
# Abra: target/site/jacoco/index.html
```

## 🐳 DevContainer

Para ambiente de desenvolvimento padronizado, use o VS Code DevContainer:

1. Instale a extensão **Dev Containers** no VS Code
2. Abra o projeto
3. `Ctrl+Shift+P` → "Dev Containers: Reopen in Container"

## 📡 API Endpoints

### Authentication

```http
POST /api/v1/auth/register  - Registrar usuário
POST /api/v1/auth/login     - Login (retorna JWT)
```

### Transactions

```http
POST   /api/v1/transactions          - Criar transação
GET    /api/v1/transactions          - Listar transações (filtros: date, category, type)
GET    /api/v1/transactions/{id}     - Obter transação
PUT    /api/v1/transactions/{id}     - Atualizar transação
DELETE /api/v1/transactions/{id}     - Deletar transação
```

### Budgets

```http
POST   /api/v1/budgets           - Criar orçamento
GET    /api/v1/budgets           - Listar orçamentos
GET    /api/v1/budgets/{id}/status  - Verificar status (alertas)
```

### Goals

```http
POST   /api/v1/goals                - Criar meta
GET    /api/v1/goals                - Listar metas
PUT    /api/v1/goals/{id}/progress  - Atualizar progresso
```

### Reports

```http
GET /api/v1/reports/monthly?month=2025-01       - Relatório mensal
GET /api/v1/reports/expenses-by-category        - Gastos por categoria
GET /api/v1/reports/balance-evolution           - Evolução do saldo
```

### AI Assistant 🤖

```http
POST /api/v1/ai/analyze-expenses    - Analisar gastos e sugerir cortes
POST /api/v1/ai/predict-expenses    - Prever gastos futuros
POST /api/v1/ai/recommend-goals     - Recomendar metas personalizadas
GET  /api/v1/ai/insights?month=X    - Gerar insights personalizados
```

## 🔐 Autenticação

A API usa **JWT** (JSON Web Tokens). Para acessar endpoints protegidos:

1. Registre um usuário em `/auth/register`
2. Faça login em `/auth/login` (receba o token JWT)
3. Inclua o token no header: `Authorization: Bearer {token}`

## 🧪 Metodologia TDD

Todo código é desenvolvido seguindo o ciclo **Red-Green-Refactor**:

1. 🔴 **Red**: Escrever teste que falha
2. 🟢 **Green**: Implementar código mínimo para passar
3. 🔵 **Refactor**: Melhorar código mantendo testes passando

**Cobertura de testes**: > 80%

## 🚀 Deploy

### Produção (Railway/Render)

O deploy é automático via GitHub Actions ao fazer push para `main`:

1. CI roda todos os testes
2. Build da imagem Docker
3. Deploy para Railway/Render
4. Health check automático

**URLs**:

- Production: https://finance-api.railway.app
- API Docs: https://finance-api.railway.app/swagger-ui.html

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma feature branch (`git checkout -b feature/nova-feature`)
3. Escreva testes **antes** de implementar
4. Commit suas mudanças (`git commit -m 'feat: adiciona nova feature'`)
5. Push para a branch (`git push origin feature/nova-feature`)
6. Abra um Pull Request

**Convenções de commit**: [Conventional Commits](https://www.conventionalcommits.org/)

## 📝 Roadmap

- [x] ✅ Definir arquitetura
- [x] ✅ Criar plano de execução
- [ ] 🚧 Setup inicial (Maven, Docker, CI/CD)
- [ ] 🚧 Domain layer (Entities, VOs)
- [ ] 🚧 Use Cases (Application layer)
- [ ] 🚧 Infrastructure (JPA, Security)
- [ ] 🚧 Web layer (Controllers)
- [ ] 🚧 Integração IA
- [ ] 🚧 Deploy produção

Ver detalhes completos em **[EXECUTION_PLAN.md](EXECUTION_PLAN.md)**

## 📄 Licença

Este projeto está sob a licença MIT. Veja [LICENSE](LICENSE) para mais detalhes.

## 👨‍💻 Autor

- GitHub: [@veidz](https://github.com/veidz)

---

⭐ Se este projeto foi útil, considere dar uma estrela!
