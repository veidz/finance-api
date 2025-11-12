# 📋 Plano de Execução - Finance API

## 🎯 Objetivo

Implementar API de Controle Financeiro com IA seguindo TDD, Clean Architecture e KISS Principle.

---

## 📊 Status Atual

### ✅ Concluído

- **FASE 1**: Setup Inicial (95% completo)

  - Estrutura de diretórios ✅
  - Maven + dependências ✅
  - Docker + Docker Compose ✅
  - CI Pipeline (GitHub Actions) ✅
  - Checkstyle + SpotBugs + JaCoCo ✅

- **FASE 2**: Domain Layer (100% completo)

  - Value Objects: Money, Email, DateRange ✅
  - Enums: TransactionType, GoalStatus ✅
  - Entities: User (15 tests), Transaction (16 tests), Category (11 tests), Budget (31 tests), FinancialGoal (33 tests) ✅
  - **Total Domain Tests**: 106 tests passing ✅

- **FASE 3**: Application Layer (10% completo)
  - CreateUserUseCase: Implementado com 8 tests ✅
  - UserRepository port interface ✅
  - DTOs: CreateUserRequest, UserResponse ✅
  - SpotBugs CI issues resolvidos ✅

### 🚧 Em Progresso

- **FASE 3**: Application Layer - Use Cases
  - **Próximo**: AuthenticateUserUseCase

### 📈 Estatísticas

- **Total de Testes**: 149 passing (106 domain + 8 application + 35 outros)
- **Cobertura de Código**: JaCoCo configurado (14 classes analisadas)
- **Qualidade de Código**:
  - Checkstyle: 0 violations
  - SpotBugs: 0 bugs
  - Build Status: ✅ Passing

### 🎯 Próximos Passos

1. Implementar `AuthenticateUserUseCase` (TDD)
2. Completar use cases de Transaction (Create, List, Update, Delete)
3. Completar use cases de Budget e Goal
4. Iniciar FASE 4: Infrastructure Layer (JPA adapters)

---

## 📦 FASE 1: SETUP INICIAL ✅

### 1.1 Setup Estrutura Base ✅

- [x] Criar estrutura de diretórios (domain, application, infrastructure, web)
- [x] Configurar `pom.xml` com dependências essenciais
- [x] Criar `.gitignore` para Java/Maven
- [x] Configurar `application.yml` (profiles: dev, prod, test)

**Dependências principais**:

```xml
- spring-boot-starter-web (3.2.x)
- spring-boot-starter-data-jpa
- spring-boot-starter-validation
- spring-boot-starter-security
- postgresql driver
- flyway-core
- lombok
- mapstruct
- springdoc-openapi (Swagger)
- spring-boot-starter-test
- testcontainers
- rest-assured
```

### 1.2 Docker & DevContainer ✅

- [x] Criar `Dockerfile` multi-stage (builder + runtime)
- [x] Criar `docker-compose.yml` (app + postgres local)
- [x] Configurar `.devcontainer/devcontainer.json`
  - Java 21
  - Maven
  - PostgreSQL client
  - Extensions VS Code

**Comandos de teste**:

```bash
docker-compose up -d
./mvnw clean package
docker build -t finance-api .
```

### 1.3 VS Code Configuration ⚠️ (Parcial)

- [ ] `.vscode/extensions.json` (Java, Spring, Docker, GitLens, Test Runner)
- [ ] `.vscode/settings.json` (formatação, Java runtime)
- [ ] `.vscode/tasks.json` (mvn: test, package, run)
- [ ] `.vscode/launch.json` (debug Spring Boot)

### 1.4 CI/CD Pipeline ✅

- [x] `.github/workflows/ci.yml`
  - Job: build
  - Job: unit-tests (com JaCoCo)
  - Job: integration-tests (Testcontainers)
  - Job: code-quality (Spotbugs, Checkstyle)
  - Job: security-scan (OWASP Dependency Check)
- [ ] `.github/workflows/cd.yml`
  - Trigger: push to main
  - Build Docker image
  - Deploy to Railway/Render
  - Health check

### 1.5 Database Setup

- [ ] Criar projeto no Supabase
- [ ] Configurar secrets no GitHub (DB credentials)
- [ ] Criar `V1__initial_schema.sql` (Flyway migration)
- [ ] Testar conexão local com Supabase

**Migrations**:

```
V1__create_users_table.sql
V2__create_categories_table.sql
V3__create_transactions_table.sql
V4__create_budgets_table.sql
V5__create_goals_table.sql
```

---

## 🧱 FASE 2: DOMAIN LAYER

### 2.1 Value Objects (TDD) ✅

**Test First** → Implementar → Refatorar

- [x] `Money` (BigDecimal + Currency, validations)

  - Test: should create valid money ✅
  - Test: should reject negative amounts ✅
  - Test: should add two money objects with same currency ✅
  - Test: should throw exception when adding different currencies ✅

- [x] `Email` (validação de formato)

  - Test: should accept valid email ✅
  - Test: should reject invalid email format ✅

- [x] `DateRange` (período com validação)

  - Test: should create valid date range ✅
  - Test: should reject start date after end date ✅

- [x] `TransactionType` (enum: INCOME, EXPENSE)

- [x] `GoalStatus` (enum: IN_PROGRESS, ACHIEVED, CANCELLED)

### 2.2 Domain Entities (TDD) ✅

#### User Entity ✅

- [x] **Test**: should create user with valid data (15 tests total)
- [x] **Test**: should validate email format
- [x] **Test**: should hash password
- [x] **Implement**: User entity with invariants
- [x] **Refactor**: extract password hashing to VO

#### Transaction Entity ✅

- [x] **Test**: should create transaction with all required fields (16 tests total)
- [x] **Test**: should calculate balance impact (income +, expense -)
- [x] **Test**: should validate amount > 0
- [x] **Test**: should link to category
- [x] **Implement**: Transaction entity
- [x] **Refactor**: extract business rules to methods

#### Category Entity ✅

- [x] **Test**: should create category with name and type (11 tests total)
- [x] **Test**: should prevent duplicate category names per user
- [x] **Implement**: Category entity

#### Budget Entity ✅

- [x] **Test**: should create budget with amount and period (31 tests total)
- [x] **Test**: should calculate spent percentage
- [x] **Test**: should alert when 80% spent
- [x] **Test**: should mark as exceeded when > 100%
- [x] **Implement**: Budget entity with alerts logic

#### FinancialGoal Entity ✅

- [x] **Test**: should create goal with target and deadline (33 tests total)
- [x] **Test**: should calculate progress percentage
- [x] **Test**: should estimate completion date based on trend
- [x] **Test**: should mark as achieved when target reached
- [x] **Implement**: FinancialGoal entity

### 2.3 Repository Interfaces (Ports) ⚠️ (Parcial)

- [x] `UserRepository` (application/ports)
- [ ] `TransactionRepository` (com métodos de filtro)
- [ ] `CategoryRepository`
- [ ] `BudgetRepository`
- [ ] `GoalRepository`

### 2.4 Service Interfaces (Ports)

- [ ] `AIAnalysisService`
- [ ] `EmailService`
- [ ] `ReportGenerator`

---

## 🎬 FASE 3: APPLICATION LAYER - USE CASES 🚧 Em Progresso

### 3.1 User Management Use Cases

#### CreateUserUseCase (TDD) ✅

- [x] **Test**: should create user with hashed password (8 tests total)
- [x] **Test**: should throw exception if email already exists
- [x] **Test**: should validate email format
- [x] **Implement**: use case
- [x] **Refactor**: extract validation
- [x] **Fix**: SpotBugs CI issues (EI_EXPOSE_REP2, CT_CONSTRUCTOR_THROW)
- [x] **Create**: UserRepository port interface
- [x] **Create**: DTOs (CreateUserRequest, UserResponse)

#### AuthenticateUserUseCase (TDD) ⏭️ Próximo

- [ ] **Test**: should authenticate with correct credentials
- [ ] **Test**: should reject wrong password
- [ ] **Test**: should reject non-existent email
- [ ] **Implement**: use case
- [ ] **Mock**: UserRepository

### 3.2 Transaction Management Use Cases

#### CreateTransactionUseCase (TDD)

- [ ] **Test**: should create transaction and link to user
- [ ] **Test**: should validate amount > 0
- [ ] **Test**: should validate category belongs to user
- [ ] **Test**: should update user balance
- [ ] **Implement**: use case
- [ ] **Mock**: TransactionRepository, CategoryRepository

#### ListTransactionsUseCase (TDD)

- [ ] **Test**: should list all user transactions
- [ ] **Test**: should filter by date range
- [ ] **Test**: should filter by category
- [ ] **Test**: should filter by type (income/expense)
- [ ] **Test**: should paginate results
- [ ] **Implement**: use case with filters

#### UpdateTransactionUseCase (TDD)

- [ ] **Test**: should update transaction fields
- [ ] **Test**: should prevent updating other user's transaction
- [ ] **Test**: should recalculate balance on amount change
- [ ] **Implement**: use case

#### DeleteTransactionUseCase (TDD)

- [ ] **Test**: should delete transaction
- [ ] **Test**: should prevent deleting other user's transaction
- [ ] **Test**: should update balance after deletion
- [ ] **Implement**: use case

### 3.3 Budget Management Use Cases

#### CreateBudgetUseCase (TDD)

- [ ] **Test**: should create budget for category and period
- [ ] **Test**: should prevent overlapping budgets for same category
- [ ] **Implement**: use case

#### CheckBudgetStatusUseCase (TDD)

- [ ] **Test**: should calculate spent amount in period
- [ ] **Test**: should return alert when 80% spent
- [ ] **Test**: should return exceeded when > 100%
- [ ] **Implement**: use case
- [ ] **Mock**: TransactionRepository, BudgetRepository

### 3.4 Goal Management Use Cases

#### CreateGoalUseCase (TDD)

- [ ] **Test**: should create goal with target amount
- [ ] **Test**: should validate target > 0
- [ ] **Implement**: use case

#### UpdateGoalProgressUseCase (TDD)

- [ ] **Test**: should update current amount
- [ ] **Test**: should mark as achieved when target reached
- [ ] **Test**: should calculate new completion estimate
- [ ] **Implement**: use case

### 3.5 Report Use Cases

#### GenerateMonthlyReportUseCase (TDD)

- [ ] **Test**: should calculate total income
- [ ] **Test**: should calculate total expenses
- [ ] **Test**: should calculate balance
- [ ] **Test**: should group expenses by category
- [ ] **Implement**: use case
- [ ] **Mock**: TransactionRepository

---

## 🔌 FASE 4: INFRASTRUCTURE LAYER

### 4.1 JPA Entities & Repositories

- [ ] Create JPA entities (UserEntity, TransactionEntity, etc.)
- [ ] Create Spring Data JPA repositories
- [ ] Create mappers (Entity ↔ Domain)
- [ ] Create repository adapters (implement domain ports)

#### Integration Tests (Testcontainers)

- [ ] **Test**: UserRepositoryAdapter CRUD operations
- [ ] **Test**: TransactionRepositoryAdapter with filters
- [ ] **Test**: BudgetRepositoryAdapter with date ranges
- [ ] **Test**: GoalRepositoryAdapter queries

### 4.2 Security Infrastructure

#### JwtTokenProvider (TDD)

- [ ] **Test**: should generate valid JWT token
- [ ] **Test**: should validate token signature
- [ ] **Test**: should extract username from token
- [ ] **Test**: should reject expired token
- [ ] **Implement**: JWT provider

#### JwtAuthenticationFilter

- [ ] **Test**: should authenticate request with valid token
- [ ] **Test**: should reject request without token
- [ ] **Test**: should reject request with invalid token
- [ ] **Implement**: filter

#### SecurityConfiguration

- [ ] Configure public endpoints (/auth/**, /swagger-ui/**)
- [ ] Configure protected endpoints (require authentication)
- [ ] Configure password encoder (BCrypt)

### 4.3 Configuration Beans

- [ ] `BeanConfiguration` (wire use cases with adapters)
- [ ] `DatabaseConfiguration` (DataSource, JPA properties)
- [ ] `CorsConfiguration` (allow frontend origin)

---

## 🌐 FASE 5: WEB LAYER - REST CONTROLLERS

### 5.1 DTOs & Mappers

- [ ] Create Request DTOs (javax.validation annotations)
- [ ] Create Response DTOs
- [ ] Create web mappers (Request/Response ↔ Application DTOs)

### 5.2 Controllers (TDD with MockMvc)

#### AuthController

- [ ] **Test**: POST /api/v1/auth/register (201 Created)
- [ ] **Test**: POST /api/v1/auth/register (400 Bad Request - invalid email)
- [ ] **Test**: POST /api/v1/auth/login (200 OK with JWT)
- [ ] **Test**: POST /api/v1/auth/login (401 Unauthorized)
- [ ] **Implement**: AuthController

#### TransactionController

- [ ] **Test**: POST /api/v1/transactions (201 Created)
- [ ] **Test**: GET /api/v1/transactions (200 OK with pagination)
- [ ] **Test**: GET /api/v1/transactions?startDate=X&endDate=Y
- [ ] **Test**: PUT /api/v1/transactions/{id} (200 OK)
- [ ] **Test**: DELETE /api/v1/transactions/{id} (204 No Content)
- [ ] **Test**: GET /api/v1/transactions/{id} (404 Not Found)
- [ ] **Implement**: TransactionController

#### BudgetController

- [ ] **Test**: POST /api/v1/budgets (201 Created)
- [ ] **Test**: GET /api/v1/budgets (200 OK)
- [ ] **Test**: GET /api/v1/budgets/{id}/status (200 OK with alert)
- [ ] **Implement**: BudgetController

#### GoalController

- [ ] **Test**: POST /api/v1/goals (201 Created)
- [ ] **Test**: GET /api/v1/goals (200 OK)
- [ ] **Test**: PUT /api/v1/goals/{id}/progress (200 OK)
- [ ] **Implement**: GoalController

#### ReportController

- [ ] **Test**: GET /api/v1/reports/monthly?month=YYYY-MM (200 OK)
- [ ] **Test**: GET /api/v1/reports/expenses-by-category (200 OK)
- [ ] **Implement**: ReportController

### 5.3 Exception Handling

- [ ] `GlobalExceptionHandler` (@ControllerAdvice)
  - MethodArgumentNotValidException → 400
  - EntityNotFoundException → 404
  - AuthenticationException → 401
  - AccessDeniedException → 403
  - Generic Exception → 500

---

## 🤖 FASE 6: IA INTEGRATION

### 6.1 Setup IA Client

**Escolha da API** (avaliar custo/benefício):

- OpenAI GPT-3.5-turbo (~$0.002/1K tokens)
- Groq Llama 3 (6000 tokens/min FREE)
- Google Gemini (FREE tier generoso)

- [ ] Adicionar dependência LangChain4j ou cliente REST
- [ ] Criar `OpenAIConfiguration` (API key via env)
- [ ] Criar `OpenAIClient` (wrapper para API)
- [ ] Criar `OpenAIAnalysisServiceAdapter` (implementa port)

### 6.2 AI Use Cases (TDD)

#### AnalyzeExpensesUseCase

- [ ] **Test**: should return expense analysis with suggestions (mock AI response)
- [ ] **Test**: should handle AI API timeout
- [ ] **Test**: should aggregate transactions by category before sending to AI
- [ ] **Implement**: use case
- [ ] **Mock**: AIAnalysisService, TransactionRepository

#### PredictFutureExpensesUseCase

- [ ] **Test**: should return predictions for next 3 months (mock)
- [ ] **Test**: should use last 6 months of data
- [ ] **Implement**: use case

#### RecommendGoalsUseCase

- [ ] **Test**: should return 3 goal recommendations (mock)
- [ ] **Test**: should consider user's income and expense patterns
- [ ] **Implement**: use case

#### GeneratePersonalizedInsightsUseCase

- [ ] **Test**: should generate friendly report in Portuguese
- [ ] **Test**: should include top expense categories
- [ ] **Implement**: use case

### 6.3 AI Controller

#### AIAssistantController

- [ ] **Test**: POST /api/v1/ai/analyze-expenses (200 OK)
- [ ] **Test**: POST /api/v1/ai/predict-expenses (200 OK)
- [ ] **Test**: POST /api/v1/ai/recommend-goals (200 OK)
- [ ] **Test**: GET /api/v1/ai/insights?month=YYYY-MM (200 OK)
- [ ] **Implement**: AIAssistantController

---

## 🧪 FASE 7: TESTES E QUALIDADE

### 7.1 Integration Tests (E2E)

- [ ] Test: User registration → Login → Create transaction → View report
- [ ] Test: Create budget → Exceed budget → Receive alert
- [ ] Test: Create goal → Update progress → Achieve goal
- [ ] Test: AI analysis flow with real transactions

### 7.2 Performance Tests (opcional)

- [ ] Load test: 100 concurrent users creating transactions
- [ ] Response time: all endpoints < 500ms (p95)

### 7.3 Quality Gates

- [ ] JaCoCo: coverage > 80%
- [ ] Spotbugs: zero high-priority bugs
- [ ] Checkstyle: zero violations
- [ ] OWASP: zero vulnerabilities (or documented exceptions)

### 7.4 Documentation

- [ ] OpenAPI/Swagger annotations in controllers
- [ ] Generate API documentation (accessible at /swagger-ui.html)
- [ ] Javadoc in domain entities and use cases
- [ ] Update README with API examples

---

## 🚀 FASE 8: DEPLOY & MONITORING

### 8.1 Preparação Deploy

#### Railway Setup

- [ ] Criar conta no Railway
- [ ] Criar projeto "finance-api"
- [ ] Adicionar PostgreSQL database
- [ ] Configurar variáveis de ambiente:
  ```
  SPRING_PROFILES_ACTIVE=prod
  DB_HOST=...
  DB_PORT=5432
  DB_NAME=railway
  DB_USER=postgres
  DB_PASSWORD=...
  JWT_SECRET=...
  OPENAI_API_KEY=...
  ```
- [ ] Conectar GitHub repository
- [ ] Configurar deploy automático (branch: main)

#### Alternativa: Render

- [ ] Criar conta no Render
- [ ] Criar Web Service (Docker)
- [ ] Adicionar PostgreSQL (free tier)
- [ ] Configurar environment variables
- [ ] Auto-deploy from GitHub

### 8.2 CI/CD Final

- [ ] Testar pipeline completo (commit → CI → deploy → health check)
- [ ] Configurar notificações (Slack/Discord/Email) em caso de falha

### 8.3 Monitoring

- [ ] Verificar Spring Boot Actuator endpoints:
  - /actuator/health
  - /actuator/metrics
  - /actuator/info
- [ ] Configurar logging estruturado (JSON format)
- [ ] (Opcional) Integrar com Sentry para error tracking

### 8.4 Smoke Tests Produção

- [ ] Criar script de smoke test (bash + curl)
  - Health check
  - Register user
  - Login
  - Create transaction
- [ ] Executar após cada deploy (CD pipeline)

---

## 📝 FASE 9: DOCUMENTAÇÃO FINAL

### 9.1 README.md

- [ ] Project overview
- [ ] Architecture diagram
- [ ] Tech stack
- [ ] Prerequisites
- [ ] Setup instructions (local dev)
- [ ] API endpoints overview
- [ ] Deploy instructions
- [ ] Contributing guidelines

### 9.2 API Documentation

- [ ] Postman collection (export)
- [ ] cURL examples for each endpoint
- [ ] Authentication flow explanation

### 9.3 Developer Docs

- [ ] ARCHITECTURE.md (já criado ✅)
- [ ] CONTRIBUTING.md (code style, PR template)
- [ ] DEPLOYMENT.md (step-by-step deploy guide)

---

## 📊 Checklist de Entrega

### Funcionalidades Core

- [ ] ✅ User registration & authentication (JWT)
- [ ] ✅ Transaction CRUD (income/expense)
- [ ] ✅ Categories management
- [ ] ✅ Budget creation & alerts
- [ ] ✅ Financial goals tracking
- [ ] ✅ Monthly reports
- [ ] ✅ Expense analysis by AI
- [ ] ✅ Expense predictions
- [ ] ✅ Goal recommendations by AI
- [ ] ✅ Personalized insights

### Qualidade

- [ ] ✅ Unit tests > 80% coverage
- [ ] ✅ Integration tests (Testcontainers)
- [ ] ✅ E2E tests (main flows)
- [ ] ✅ Zero critical vulnerabilities
- [ ] ✅ API documented (Swagger)

### DevOps

- [ ] ✅ Docker multi-stage build
- [ ] ✅ Docker Compose for local dev
- [ ] ✅ DevContainer configured
- [ ] ✅ CI pipeline (build + tests + quality)
- [ ] ✅ CD pipeline (auto-deploy)
- [ ] ✅ App deployed to Railway/Render
- [ ] ✅ Database migrations automated (Flyway)

### Documentação

- [ ] ✅ README completo
- [ ] ✅ ARCHITECTURE.md
- [ ] ✅ API docs (Swagger UI)
- [ ] ✅ Postman collection

---

## ⚠️ Decisões Pendentes (Perguntar ao Cliente)

1. **API de IA**: Qual usar?

   - OpenAI GPT-3.5 (pago, ~$0.002/1K tokens)
   - Groq Llama 3 (grátis, 6000 tokens/min)
   - Google Gemini (grátis com limite)

2. **Plataforma de Deploy**: Railway ou Render?

   - Railway: $5 crédito/mês, mais simples
   - Render: Free tier permanente, mas com cold start

3. **Idioma da API**:

   - Responses em PT-BR ou EN?
   - Mensagens de erro em que idioma?

4. **Autenticação**:

   - JWT simples ou OAuth2?
   - Multi-tenancy (empresas) ou apenas usuários individuais?

5. **Rate Limiting**:
   - Limitar requests de IA por usuário? (custos)

---

## 🎯 Métricas de Sucesso

- **Code Coverage**: > 80%
- **Build Time**: < 5 minutos
- **Deploy Time**: < 3 minutos
- **API Response Time**: p95 < 500ms
- **Zero Downtime Deployments**: ✅
- **Security Vulnerabilities**: 0 critical/high

---

## 📅 Cronograma Sugerido

| Fase | Entregas                                                |
| ---- | ------------------------------------------------------- |
| 1    | Setup: Estrutura, Docker, CI/CD, DB                     |
| 2    | Domain: Entities, VOs, Ports                            |
| 3    | Use Cases: User, Transaction, Budget, Goal              |
| 4    | Infrastructure: JPA, Security, Config                   |
| 5    | Web Layer: Controllers, DTOs, Exception handling        |
| 6    | IA Integration: AI client, Use cases, Controller        |
| 7    | Testes & Quality: Integration tests, E2E, Quality gates |
| 8    | Deploy & Docs: Produção, Monitoring, Docs               |

---

## 🚀 Próximos Passos

1. ✅ Aprovar arquitetura
2. Responder decisões pendentes
3. Executar Fase 1.1 (criar estrutura Maven)
4. Seguir plano TDD rigorosamente
5. Commits frequentes (feature branches + PRs)

---

**Instruções ao Copilot**:

- Sempre seguir ciclo TDD (test first)
- Implementar somente o necessário (KISS)
- Manter métodos curtos (< 25 linhas)
- Nomear testes: `shouldReturnExpectedWhenCondition`
- Commits atômicos: uma feature por vez
- Perguntar antes de implementar features não especificadas (YAGNI)
