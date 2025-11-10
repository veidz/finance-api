# Arquitetura - Finance API

## 📋 Visão Geral

API REST para Controle Financeiro Pessoal com Assistente de Análise por IA, desenvolvida em **Java 17 + Spring Boot 3.x**, seguindo princípios de **Clean Architecture**, **TDD**, **KISS** e **Design Patterns**.

## 🏗️ Arquitetura Hexagonal (Clean Architecture)

```
finance-api/
├── src/
│   ├── main/
│   │   ├── java/com/veidz/financeapi/
│   │   │   ├── domain/                    # Core - Regras de Negócio (Entities, VOs, Interfaces)
│   │   │   │   ├── entities/
│   │   │   │   │   ├── User.java
│   │   │   │   │   ├── Transaction.java
│   │   │   │   │   ├── Category.java
│   │   │   │   │   ├── Budget.java
│   │   │   │   │   ├── FinancialGoal.java
│   │   │   │   │   └── Report.java
│   │   │   │   ├── valueobjects/
│   │   │   │   │   ├── Money.java         # BigDecimal + Currency
│   │   │   │   │   ├── Email.java
│   │   │   │   │   ├── DateRange.java
│   │   │   │   │   ├── TransactionType.java (enum)
│   │   │   │   │   └── GoalStatus.java (enum)
│   │   │   │   ├── ports/                 # Interfaces (Driven Ports)
│   │   │   │   │   ├── repositories/
│   │   │   │   │   │   ├── UserRepository.java
│   │   │   │   │   │   ├── TransactionRepository.java
│   │   │   │   │   │   ├── BudgetRepository.java
│   │   │   │   │   │   └── GoalRepository.java
│   │   │   │   │   └── services/
│   │   │   │   │       ├── AIAnalysisService.java
│   │   │   │   │       ├── EmailService.java
│   │   │   │   │       └── ReportGenerator.java
│   │   │   │   └── exceptions/
│   │   │   │       ├── DomainException.java
│   │   │   │       ├── EntityNotFoundException.java
│   │   │   │       └── ValidationException.java
│   │   │   │
│   │   │   ├── application/               # Use Cases (Business Rules Orchestration)
│   │   │   │   ├── usecases/
│   │   │   │   │   ├── user/
│   │   │   │   │   │   ├── CreateUserUseCase.java
│   │   │   │   │   │   ├── AuthenticateUserUseCase.java
│   │   │   │   │   │   └── UpdateUserProfileUseCase.java
│   │   │   │   │   ├── transaction/
│   │   │   │   │   │   ├── CreateTransactionUseCase.java
│   │   │   │   │   │   ├── ListTransactionsUseCase.java
│   │   │   │   │   │   ├── UpdateTransactionUseCase.java
│   │   │   │   │   │   ├── DeleteTransactionUseCase.java
│   │   │   │   │   │   └── GetTransactionsByPeriodUseCase.java
│   │   │   │   │   ├── budget/
│   │   │   │   │   │   ├── CreateBudgetUseCase.java
│   │   │   │   │   │   ├── UpdateBudgetUseCase.java
│   │   │   │   │   │   ├── CheckBudgetStatusUseCase.java
│   │   │   │   │   │   └── GetBudgetAlertsUseCase.java
│   │   │   │   │   ├── goal/
│   │   │   │   │   │   ├── CreateGoalUseCase.java
│   │   │   │   │   │   ├── UpdateGoalProgressUseCase.java
│   │   │   │   │   │   ├── ListGoalsUseCase.java
│   │   │   │   │   │   └── CalculateGoalProjectionUseCase.java
│   │   │   │   │   ├── report/
│   │   │   │   │   │   ├── GenerateMonthlyReportUseCase.java
│   │   │   │   │   │   ├── GenerateExpenseByCategoryUseCase.java
│   │   │   │   │   │   └── GenerateBalanceEvolutionUseCase.java
│   │   │   │   │   └── ai/
│   │   │   │   │       ├── AnalyzeExpensesUseCase.java
│   │   │   │   │       ├── PredictFutureExpensesUseCase.java
│   │   │   │   │       ├── RecommendGoalsUseCase.java
│   │   │   │   │       └── GeneratePersonalizedInsightsUseCase.java
│   │   │   │   └── dto/                   # DTOs para comunicação entre camadas
│   │   │   │       ├── UserDTO.java
│   │   │   │       ├── TransactionDTO.java
│   │   │   │       └── ...
│   │   │   │
│   │   │   ├── infrastructure/            # Adapters (Framework & Drivers)
│   │   │   │   ├── persistence/
│   │   │   │   │   ├── jpa/
│   │   │   │   │   │   ├── entities/      # JPA Entities
│   │   │   │   │   │   │   ├── UserEntity.java
│   │   │   │   │   │   │   ├── TransactionEntity.java
│   │   │   │   │   │   │   ├── BudgetEntity.java
│   │   │   │   │   │   │   └── GoalEntity.java
│   │   │   │   │   │   ├── repositories/  # Spring Data JPA Repositories
│   │   │   │   │   │   │   ├── JpaUserRepository.java
│   │   │   │   │   │   │   ├── JpaTransactionRepository.java
│   │   │   │   │   │   │   ├── JpaBudgetRepository.java
│   │   │   │   │   │   │   └── JpaGoalRepository.java
│   │   │   │   │   │   └── mappers/       # Entity <-> Domain mappers
│   │   │   │   │   │       ├── UserMapper.java
│   │   │   │   │   │       └── TransactionMapper.java
│   │   │   │   │   └── adapters/          # Repository Implementations
│   │   │   │   │       ├── UserRepositoryAdapter.java
│   │   │   │   │       ├── TransactionRepositoryAdapter.java
│   │   │   │   │       ├── BudgetRepositoryAdapter.java
│   │   │   │   │       └── GoalRepositoryAdapter.java
│   │   │   │   ├── ai/
│   │   │   │   │   ├── openai/
│   │   │   │   │   │   ├── OpenAIClient.java
│   │   │   │   │   │   ├── OpenAIConfiguration.java
│   │   │   │   │   │   └── OpenAIAnalysisServiceAdapter.java
│   │   │   │   │   └── dto/
│   │   │   │   │       ├── AIPromptRequest.java
│   │   │   │   │       └── AIAnalysisResponse.java
│   │   │   │   ├── email/
│   │   │   │   │   └── EmailServiceAdapter.java
│   │   │   │   ├── security/
│   │   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   │   ├── SecurityConfiguration.java
│   │   │   │   │   └── UserDetailsServiceImpl.java
│   │   │   │   └── config/
│   │   │   │       ├── DatabaseConfiguration.java
│   │   │   │       ├── BeanConfiguration.java     # Wire use cases
│   │   │   │       └── CorsConfiguration.java
│   │   │   │
│   │   │   └── web/                       # Controllers (Driving Adapters)
│   │   │       ├── controllers/
│   │   │       │   ├── UserController.java
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── TransactionController.java
│   │   │       │   ├── BudgetController.java
│   │   │       │   ├── GoalController.java
│   │   │       │   ├── ReportController.java
│   │   │       │   └── AIAssistantController.java
│   │   │       ├── dto/                   # Request/Response DTOs
│   │   │       │   ├── request/
│   │   │       │   │   ├── CreateUserRequest.java
│   │   │       │   │   ├── LoginRequest.java
│   │   │       │   │   ├── CreateTransactionRequest.java
│   │   │       │   │   └── ...
│   │   │       │   └── response/
│   │   │       │       ├── UserResponse.java
│   │   │       │       ├── JwtResponse.java
│   │   │       │       ├── TransactionResponse.java
│   │   │       │       ├── ErrorResponse.java
│   │   │       │       └── ...
│   │   │       ├── mappers/               # Request/Response <-> DTO mappers
│   │   │       │   ├── UserWebMapper.java
│   │   │       │   └── TransactionWebMapper.java
│   │   │       └── advice/
│   │   │           └── GlobalExceptionHandler.java
│   │   │
│   │   └── resources/
│   │       ├── application.yml            # Config principal
│   │       ├── application-dev.yml        # Profile dev
│   │       ├── application-prod.yml       # Profile prod
│   │       └── db/migration/              # Flyway migrations
│   │           ├── V1__create_users_table.sql
│   │           ├── V2__create_transactions_table.sql
│   │           ├── V3__create_categories_table.sql
│   │           ├── V4__create_budgets_table.sql
│   │           └── V5__create_goals_table.sql
│   │
│   └── test/
│       ├── java/com/veidz/financeapi/
│       │   ├── domain/                    # Unit tests - Entities & VOs
│       │   ├── application/               # Unit tests - Use Cases
│       │   ├── infrastructure/            # Integration tests
│       │   └── web/                       # Integration tests - Controllers
│       └── resources/
│           └── application-test.yml
│
├── .github/
│   ├── workflows/
│   │   ├── ci.yml                         # CI Pipeline
│   │   └── cd.yml                         # CD Pipeline
│   └── copilot-instructions.md
│
├── .devcontainer/
│   ├── devcontainer.json
│   └── Dockerfile
│
├── .vscode/
│   ├── extensions.json
│   ├── settings.json
│   ├── tasks.json
│   └── launch.json
│
├── docker/
│   ├── Dockerfile                         # Multi-stage production build
│   └── docker-compose.yml                 # Dev environment
│
├── docs/
│   ├── api/                               # API documentation
│   └── diagrams/                          # Architecture diagrams
│
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
├── README.md
├── ARCHITECTURE.md
└── LICENSE
```

## 🎯 Stack Tecnológica

### Backend

- **Java 17** (LTS)
- **Spring Boot 3.2+**
- **Spring Data JPA** (Persistência)
- **Spring Security** (Autenticação/Autorização)
- **Spring Validation** (Bean Validation)
- **Flyway** (Database Migrations)
- **MapStruct** (Mapeamento objeto-objeto)
- **Lombok** (Redução de boilerplate - uso moderado)

### Database

- **Supabase PostgreSQL** (Cloud Database - Free Tier)
  - Plano gratuito: 500MB storage, 2GB bandwidth/month
  - PostgreSQL 15
  - Connection pooling incluso
  - Backup automático

### IA Integration

- **OpenAI API** (GPT-4 Turbo ou GPT-3.5-turbo)
  - Alternativa gratuita: **Groq API** (Llama 3 - até 6000 tokens/min grátis)
  - Outra opção: **Google Gemini API** (Free tier generoso)
- **LangChain4j** (Framework Java para LLMs)

### Testing

- **JUnit 5** (Unit tests)
- **Mockito** (Mocking)
- **AssertJ** (Fluent assertions)
- **Testcontainers** (Integration tests com Docker)
- **REST Assured** (API testing)
- **JaCoCo** (Code coverage)

### Quality & Security

- **Spotbugs** (Static analysis)
- **Checkstyle** (Code style)
- **SonarLint** (IDE integration)
- **OWASP Dependency Check** (Vulnerability scanning)

### Documentation

- **SpringDoc OpenAPI** (Swagger/OpenAPI 3.0)
- **Javadoc**

### Monitoring & Observability

- **Spring Boot Actuator** (Metrics & Health)
- **Micrometer** (Metrics collection)
- **Logback** (Structured logging)

### DevOps

- **Docker** (Containerização)
- **Docker Compose** (Dev environment)
- **GitHub Actions** (CI/CD)
- **Railway** ou **Render** (Deploy - Free tier)

### Development

- **VS Code** (IDE)
- **Dev Containers** (Ambiente padronizado)
- **Maven** (Build)
- **Git** (Version control)

## 💾 Modelo de Dados

### Schema PostgreSQL (Supabase)

```sql
-- Users
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Categories
CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('INCOME', 'EXPENSE')),
    icon VARCHAR(50),
    color VARCHAR(7),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(user_id, name)
);

-- Transactions
CREATE TABLE transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    category_id UUID REFERENCES categories(id) ON DELETE SET NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('INCOME', 'EXPENSE')),
    amount DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'BRL',
    description TEXT,
    transaction_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Budgets
CREATE TABLE budgets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    category_id UUID REFERENCES categories(id) ON DELETE CASCADE,
    amount DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'BRL',
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(user_id, category_id, period_start)
);

-- Financial Goals
CREATE TABLE financial_goals (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    target_amount DECIMAL(15,2) NOT NULL,
    current_amount DECIMAL(15,2) NOT NULL DEFAULT 0,
    currency VARCHAR(3) NOT NULL DEFAULT 'BRL',
    target_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS' CHECK (status IN ('IN_PROGRESS', 'ACHIEVED', 'CANCELLED')),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- AI Analysis History (opcional - para cache/histórico)
CREATE TABLE ai_analyses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    analysis_type VARCHAR(50) NOT NULL,
    prompt TEXT NOT NULL,
    response TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Indexes
CREATE INDEX idx_transactions_user_id ON transactions(user_id);
CREATE INDEX idx_transactions_date ON transactions(transaction_date);
CREATE INDEX idx_transactions_user_date ON transactions(user_id, transaction_date);
CREATE INDEX idx_budgets_user_period ON budgets(user_id, period_start, period_end);
CREATE INDEX idx_goals_user_status ON financial_goals(user_id, status);
```

## 🔐 Segurança

1. **Autenticação**: JWT (access token + refresh token)
2. **Autorização**: Spring Security com roles
3. **Senha**: BCrypt hashing
4. **HTTPS**: Obrigatório em produção
5. **CORS**: Configurado para frontend específico
6. **Rate Limiting**: Bucket4j (limitar requests por usuário)
7. **Secrets**: Variáveis de ambiente (nunca no código)

## 🤖 Integração IA - Estratégia

### 1. Análise de Gastos e Sugestões de Cortes

```java
// Prompt exemplo
"Analyze the following user expenses for the last 3 months:
[JSON com transações agrupadas por categoria]
Suggest 3-5 actionable ways to reduce expenses, prioritizing:
- Non-essential spending
- Categories with highest spending
- Recurring expenses that can be optimized
Provide response in JSON format with: category, suggestion, estimated_savings"
```

### 2. Previsão de Gastos Futuros

```java
// Análise de séries temporais via LLM
"Based on the following monthly expense history:
[JSON com totais mensais por categoria dos últimos 6 meses]
Predict expenses for the next 3 months, considering:
- Seasonal patterns
- Growth trends
- Unusual spikes
Return JSON with: month, predicted_amount, confidence_level, reasoning"
```

### 3. Recomendação de Metas Financeiras

```java
// Personalização baseada em perfil
"User financial profile:
- Monthly income: R$ X
- Average monthly expenses: R$ Y
- Current savings: R$ Z
- Spending patterns: [resumo]
Recommend 3 realistic financial goals with:
- Goal description
- Target amount
- Suggested timeline
- Steps to achieve
Format as JSON array"
```

### 4. Relatórios Explicativos

```java
// Natural language insights
"Generate a friendly financial summary for the user:
- Total income: R$ X
- Total expenses: R$ Y
- Top 3 expense categories: [list]
- Budget status: [over/under budget]
Provide encouraging feedback and 2-3 tips in Portuguese (pt-BR)"
```

## 🚀 CI/CD Pipeline

### CI (GitHub Actions)

```yaml
# .github/workflows/ci.yml
name: CI Pipeline

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - Checkout code
      - Setup Java 17
      - Cache Maven dependencies
      - Run: mvn clean compile

  unit-tests:
    needs: build
    runs-on: ubuntu-latest
    steps:
      - Run: mvn test
      - Upload coverage to Codecov

  integration-tests:
    needs: unit-tests
    runs-on: ubuntu-latest
    services:
      postgres:
        image: postgres:15-alpine
    steps:
      - Run: mvn verify -P integration-tests

  code-quality:
    needs: unit-tests
    runs-on: ubuntu-latest
    steps:
      - Run: mvn spotbugs:check
      - Run: mvn checkstyle:check
      - SonarCloud scan (opcional)

  security-scan:
    needs: build
    runs-on: ubuntu-latest
    steps:
      - Run: mvn dependency-check:check
      - Trivy container scan
```

### CD (Deploy)

**Opção 1: Railway** (Recomendado)

- Free tier: $5 crédito/mês
- Deploy automático via GitHub
- PostgreSQL incluso
- Zero config

**Opção 2: Render**

- Free tier web services
- Auto-deploy from GitHub
- PostgreSQL grátis (90 dias)

```yaml
# .github/workflows/cd.yml
name: CD Pipeline

on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - Build Docker image
      - Push to registry
      - Deploy to Railway/Render
      - Run smoke tests
      - Send notification
```

## 📊 Design Patterns Utilizados

1. **Hexagonal Architecture** (Ports & Adapters)
2. **Repository Pattern** (Abstração de persistência)
3. **Factory Pattern** (Criação de entidades)
4. **Strategy Pattern** (Diferentes tipos de relatórios)
5. **Builder Pattern** (Construção de DTOs complexos)
6. **Adapter Pattern** (Integração com serviços externos)
7. **Command Pattern** (Use cases)
8. **Value Object Pattern** (Money, Email, etc)
9. **Specification Pattern** (Queries complexas - opcional)

## 🧪 Estratégia TDD

### Ciclo Red-Green-Refactor

1. **Red**: Escrever teste que falha
2. **Green**: Implementar código mínimo para passar
3. **Refactor**: Melhorar código mantendo testes passando

### Pirâmide de Testes

```
        /\
       /E2E\          <- Poucos (smoke tests principais)
      /------\
     /  API  \        <- Alguns (controllers + integração)
    /----------\
   /   Unit     \     <- Muitos (entities, VOs, use cases)
  /--------------\
```

### Coverage Target

- **Unit tests**: > 80%
- **Integration tests**: Cenários críticos
- **E2E tests**: Fluxos principais

## 🔧 Configuração Supabase

1. Criar projeto em: https://supabase.com
2. Obter credenciais:
   - `DB_HOST`: db.xxx.supabase.co
   - `DB_PORT`: 5432
   - `DB_NAME`: postgres
   - `DB_USER`: postgres
   - `DB_PASSWORD`: [senha do projeto]
3. Connection string:
   ```
   jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}?sslmode=require
   ```

## 🌐 Deploy Endpoints

### Staging (Develop)

- API: https://finance-api-staging.railway.app
- Docs: https://finance-api-staging.railway.app/swagger-ui.html

### Production (Main)

- API: https://finance-api.railway.app
- Docs: https://finance-api.railway.app/swagger-ui.html

## 📈 Roadmap de Implementação

**Fase 1 - Setup (Sprint 1)**

- ✅ Estrutura do projeto
- ✅ DevContainer + Docker
- ✅ CI/CD básico
- ✅ Configuração DB

**Fase 2 - Core Domain (Sprint 2-3)**

- User management
- Transaction CRUD
- Categories

**Fase 3 - Features Financeiras (Sprint 4-5)**

- Budgets
- Financial Goals
- Reports básicos

**Fase 4 - IA Integration (Sprint 6-7)**

- OpenAI setup
- Expense analysis
- Predictions
- Recommendations

**Fase 5 - Polish & Deploy (Sprint 8)**

- Documentation
- Performance tuning
- Security hardening
- Production deploy

## 🎓 Referências

- Clean Architecture (Robert C. Martin)
- Domain-Driven Design (Eric Evans)
- Spring Boot Best Practices
- Effective Java (Joshua Bloch)
