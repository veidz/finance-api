# GitHub Workflows - Finance API

## 📋 Overview

Configuração de CI/CD para Finance API usando GitHub Actions.

## 🔄 Workflows

### CI - Build and Test (`ci.yml`)

**Triggers:**

- Push para `main` ou `develop`
- Pull Requests para `main` ou `develop`

**Steps:**

1. **Build**: Compila código com Maven
2. **Unit Tests**: Executa testes unitários
3. **Integration Tests**: Executa testes de integração com Testcontainers
4. **Code Coverage**: Gera relatório JaCoCo (mínimo 80%)
5. **Checkstyle**: Valida estilo de código (Google Style)
6. **SpotBugs**: Análise estática de bugs
7. **OWASP**: Verifica vulnerabilidades em dependências
8. **Artifacts**: Upload de relatórios (testes, coverage, spotbugs)
9. **PR Comment**: Comenta no PR com cobertura de código

**Services:**

- PostgreSQL 15 (para testes de integração)

### CD - Deploy (`cd.yml`)

**Triggers:**

- Push para `main`
- Tags `v*` (releases)

**Steps:**

1. **Build Docker Image**: Multi-stage build otimizado
2. **Push to GitHub Container Registry**: Publica imagem
3. **Deploy to Railway**: Deploy automático (se configurado)
4. **Deploy to Render**: Deploy automático (se configurado)
5. **GitHub Release**: Cria release para tags (com JAR)

**Required Secrets:**

- `RAILWAY_TOKEN` (opcional)
- `RAILWAY_SERVICE_NAME` (opcional)
- `RENDER_API_KEY` (opcional)
- `RENDER_SERVICE_ID` (opcional)

### Dependabot (`dependabot.yml`)

**Frequency**: Semanal (segundas-feiras, 09:00)

**Package Ecosystems:**

- **Maven**: Dependências Java
- **GitHub Actions**: Atualiza workflows
- **Docker**: Atualiza imagens base

**Configuration:**

- Max 5 PRs de Maven por vez
- Max 3 PRs de Actions/Docker por vez
- Labels automáticas: `dependencies`, `java`, `github-actions`, `docker`
- Commits convencionais: `chore(deps): ...`

## 🔐 Secrets Configuration

### GitHub Container Registry (Automático)

- `GITHUB_TOKEN` - Gerado automaticamente pelo GitHub

### Railway (Opcional)

```bash
# No GitHub: Settings > Secrets and variables > Actions > New repository secret
RAILWAY_TOKEN=<seu-token>
RAILWAY_SERVICE_NAME=<nome-do-servico>
```

### Render (Opcional)

```bash
# No GitHub: Settings > Secrets and variables > Actions > New repository secret
RENDER_API_KEY=<sua-api-key>
RENDER_SERVICE_ID=<id-do-servico>
```

## 📊 Quality Gates

### Coverage

- **Mínimo Overall**: 80%
- **Mínimo Changed Files**: 80%
- PR bloqueado se coverage < 80%

### Checkstyle

- Google Java Style Guide
- Warnings tratados como erros

### SpotBugs

- High priority bugs bloqueiam build
- Medium e low são warnings

### OWASP

- Verifica CVEs conhecidas
- Continue-on-error (não bloqueia por padrão)

## 🚀 Usage

### PR Workflow

```bash
git checkout -b feat/my-feature
# ... make changes ...
git commit -m "feat: add new feature"
git push origin feat/my-feature
# Create PR on GitHub
# CI runs automatically
# Merge after CI passes
```

### Release Workflow

```bash
# Create and push tag
git tag -a v0.1.0 -m "Release v0.1.0"
git push origin v0.1.0
# CD runs automatically
# Docker image published
# GitHub Release created
```

### Deploy Workflow

```bash
# Just merge to main
git checkout main
git merge feat/my-feature
git push origin main
# CD runs automatically
# Deploys to Railway/Render (if configured)
```

## 📈 Monitoring

### Build Status

- ✅ All checks passed
- ❌ Failed (check logs)
- 🟡 Running

### Artifacts

- **Test Results**: Surefire + Failsafe reports
- **Coverage Report**: JaCoCo HTML
- **SpotBugs Report**: XML

### Codecov Integration

- Automatic upload to Codecov
- Coverage trends over time
- PR comments with diff

## 🛠️ Local Testing

### Run CI locally (Act)

```bash
# Install act: https://github.com/nektos/act
act -j build

# With specific event
act pull_request -j build
```

### Run all checks

```bash
# Same as CI
mvn clean verify \
  -P integration-tests \
  jacoco:report \
  jacoco:check \
  checkstyle:check \
  spotbugs:check
```

## 📝 Notes

- CI roda em ~5-7 minutos
- CD roda em ~3-5 minutos
- Dependabot cria PRs automaticamente
- PRs de Dependabot passam por CI antes de merge
- Docker images são cacheadas (GitHub Actions Cache)

## 🔗 Links

- [GitHub Actions Docs](https://docs.github.com/en/actions)
- [Dependabot Docs](https://docs.github.com/en/code-security/dependabot)
- [Railway Docs](https://docs.railway.app/)
- [Render Docs](https://render.com/docs)
