# 🐳 Docker Setup - Finance API

## 📋 Overview

Este projeto **roda 100% em Docker**. Não é necessário instalar Java, Maven ou PostgreSQL localmente. Tudo é executado via Docker Compose.

## 🛠️ Arquivos

- **`Dockerfile`** - Multi-stage para produção (builder + runtime)
- **`docker-compose.yml`** - Produção (app + PostgreSQL)
- **`docker-compose.dev.yml`** - Desenvolvimento (app + PostgreSQL + hot-reload)
- **`.devcontainer/`** - VS Code DevContainer
- **`.dockerignore`** - Otimiza build
- **`scripts/`** - Scripts auxiliares

## ✅ Pré-requisitos

- **Docker Desktop** (ou Docker Engine + Docker Compose)
- **Git**
- Nada mais! 🎉

## 🚀 Quick Start - Desenvolvimento

### 1. Clone e Configure

```bash
git clone https://github.com/veidz/finance-api.git
cd finance-api

# Criar .env a partir do exemplo
cp .env.example .env
# Edite .env com suas credenciais (AI_API_KEY, etc)
```

### 2. Iniciar Ambiente

```bash
# Opção A: Usar script (recomendado)
./scripts/start-dev.sh

# Opção B: Docker Compose direto
docker-compose -f docker-compose.dev.yml up -d
docker-compose -f docker-compose.dev.yml logs -f app
```

Aguarde o Spring Boot iniciar (~40s primeira vez, ~20s próximas).

### 3. Acessar

- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Health Check**: http://localhost:8080/api/health
- **Actuator Health**: http://localhost:8080/actuator/health
- **Metrics**: http://localhost:8080/actuator/metrics

### 4. Desenvolvimento

O código em `src/` está montado como volume. Mudanças são detectadas automaticamente (hot-reload via Spring Boot DevTools).

```bash
# Edite qualquer arquivo em src/
# Salve
# Spring Boot recarrega automaticamente 🔥
```

## �️ Scripts Úteis

### Gerenciamento

```bash
./scripts/start-dev.sh      # Inicia ambiente dev
./scripts/stop-dev.sh       # Para ambiente dev
./scripts/rebuild-dev.sh    # Rebuild completo
./scripts/logs-dev.sh       # Ver logs (padrão: app)
./scripts/logs-dev.sh postgres  # Ver logs do PostgreSQL
```

### Database

```bash
./scripts/psql.sh           # Conectar ao PostgreSQL via psql

# Dentro do psql:
\dt                         # Listar tabelas
\d users                    # Descrever tabela users
SELECT * FROM users;        # Query
\q                          # Sair
```

### Docker Compose Direto

```bash
# Ver status
docker-compose -f docker-compose.dev.yml ps

# Parar um serviço específico
docker-compose -f docker-compose.dev.yml stop app

# Restart um serviço
docker-compose -f docker-compose.dev.yml restart app

# Ver logs específicos
docker-compose -f docker-compose.dev.yml logs -f postgres

# Executar comando no container
docker-compose -f docker-compose.dev.yml exec app /bin/sh

# Rebuild apenas app
docker-compose -f docker-compose.dev.yml build app
docker-compose -f docker-compose.dev.yml up -d app
```

## 🗄️ PostgreSQL

### Conexão Externa (DBeaver, DataGrip, etc)

- **Host**: localhost
- **Port**: 5432
- **Database**: financeapi_dev
- **User**: postgres
- **Password**: postgres

### PgAdmin (Interface Web)

```bash
# Iniciar PgAdmin
docker-compose -f docker-compose.dev.yml --profile admin up -d pgadmin

# Acessar
open http://localhost:5050
# Login: admin@finance.local / admin
```

**Adicionar servidor no PgAdmin:**

1. Servers → Create → Server
2. Name: `Finance API Local`
3. Connection:
   - Host: `postgres` (nome do serviço Docker)
   - Port: `5432`
   - Database: `financeapi_dev`
   - Username: `postgres`
   - Password: `postgres`

### Backup e Restore

```bash
# Backup
docker-compose -f docker-compose.dev.yml exec postgres \
  pg_dump -U postgres financeapi_dev > backup_$(date +%Y%m%d).sql

# Restore
cat backup_20250110.sql | \
  docker-compose -f docker-compose.dev.yml exec -T postgres \
  psql -U postgres financeapi_dev

# Reset completo (PERDA DE DADOS!)
docker-compose -f docker-compose.dev.yml down -v
docker-compose -f docker-compose.dev.yml up -d
```

## 🎯 Testes

### Executar testes dentro do container

```bash
# Unit tests
docker-compose -f docker-compose.dev.yml exec app mvn test

# Integration tests
docker-compose -f docker-compose.dev.yml exec app mvn verify -P integration-tests

# Coverage
docker-compose -f docker-compose.dev.yml exec app mvn test jacoco:report
# Ver: target/site/jacoco/index.html
```

## 🐛 Debug Remoto

O ambiente de desenvolvimento expõe a porta **5005** para debug.

### IntelliJ IDEA

1. Run → Edit Configurations
2. Add → Remote JVM Debug
3. Host: `localhost`
4. Port: `5005`
5. Click Debug

### VS Code

Adicione em `.vscode/launch.json`:

```json
{
  "type": "java",
  "name": "Debug Finance API (Docker)",
  "request": "attach",
  "hostName": "localhost",
  "port": 5005
}
```

## 📦 Produção

### Build da Imagem

```bash
# Build otimizado (multi-stage)
docker-compose build

# Tag para registry
docker tag finance-api:latest your-registry/finance-api:0.1.0
docker push your-registry/finance-api:0.1.0
```

### Deploy com Docker Compose

```bash
# Edite docker-compose.yml com credenciais de produção
export DB_PASSWORD=your-secure-password
export JWT_SECRET=your-jwt-secret
export AI_API_KEY=your-ai-key

# Deploy
docker-compose up -d

# Ver logs
docker-compose logs -f app

# Health check
curl http://your-server:8080/actuator/health
```

### Environment Variables (Produção)

Sempre use variáveis de ambiente, nunca hardcode:

```bash
SPRING_PROFILES_ACTIVE=prod
DB_HOST=your-db-host.supabase.co
DB_PORT=5432
DB_NAME=postgres
DB_USER=postgres
DB_PASSWORD=<secret>
JWT_SECRET=<min-256-bits>
JWT_EXPIRATION=86400000
AI_PROVIDER=openai
AI_API_KEY=<secret>
AI_MODEL=gpt-3.5-turbo
CORS_ALLOWED_ORIGINS=https://your-frontend.com
```

## 🔍 Monitoramento

### Health Checks

```bash
# Health básico
curl http://localhost:8080/actuator/health

# Health detalhado (dev only)
curl http://localhost:8080/actuator/health | jq .

# Status dos containers
docker-compose ps
```

### Métricas

```bash
# Prometheus metrics
curl http://localhost:8080/actuator/metrics

# Métrica específica
curl http://localhost:8080/actuator/metrics/jvm.memory.used

# Info
curl http://localhost:8080/actuator/info
```

### Resources Usage

```bash
# Ver uso de recursos
docker stats

# Específico do app
docker stats finance-api-dev
```

## 🐛 Troubleshooting

### Porta em uso

```bash
# Linux/Mac
lsof -i :8080
lsof -i :5432

# Windows
netstat -ano | findstr :8080
netstat -ano | findstr :5432

```

### Build falhando

```bash
# Limpar cache do Docker
docker-compose -f docker-compose.dev.yml build --no-cache

# Remover volumes órfãos
docker volume prune

# Rebuild completo
./scripts/rebuild-dev.sh
```

### App não inicia

```bash
# Ver logs completos
docker-compose -f docker-compose.dev.yml logs app

# Ver últimas 100 linhas
docker-compose -f docker-compose.dev.yml logs --tail=100 app

# Verificar se PostgreSQL está saudável
docker-compose -f docker-compose.dev.yml ps postgres
```

### Hot-reload não funciona

```bash
# Verificar se volume está montado
docker-compose -f docker-compose.dev.yml exec app ls -la /app/src

# Restart do container
docker-compose -f docker-compose.dev.yml restart app
```

### Limpar tudo

```bash
# Parar e remover containers
docker-compose -f docker-compose.dev.yml down

# Remover volumes (PERDA DE DADOS!)
docker-compose -f docker-compose.dev.yml down -v

# Remover imagens
docker rmi finance-api:latest

# Limpar sistema completo (cuidado!)
docker system prune -a --volumes
```

## 🎓 Conceitos Docker

### Multi-stage Build

Nosso `Dockerfile` tem 2 stages:

1. **builder**: Maven + compila código (imagem grande)
2. **runtime**: Apenas JRE + JAR final (imagem pequena)

Resultado: Imagem final ~200MB (vs ~800MB sem multi-stage)

### Volumes

```bash
# Listar volumes
docker volume ls | grep finance-api

# Inspecionar volume
docker volume inspect finance-api_postgres_dev_data

# Backup de volume
docker run --rm -v finance-api_postgres_dev_data:/data \
  -v $(pwd):/backup alpine tar czf /backup/postgres-backup.tar.gz -C /data .
```

### Networks

```bash
# Listar networks
docker network ls | grep finance

# Inspecionar network
docker network inspect finance-api_finance-network

# Ver containers conectados
docker network inspect finance-api_finance-network | jq '.[].Containers'
```

## 📝 Best Practices

✅ **Sempre use Docker Compose** (nunca `docker run` manual)
✅ **Variáveis de ambiente** para configuração
✅ **Volumes** para persistência
✅ **Health checks** em todos os serviços
✅ **Multi-stage builds** para imagens menores
✅ **Non-root user** nos containers
✅ **.dockerignore** para builds rápidos
✅ **Logs estruturados** (JSON em produção)

## 🔗 Links Úteis

- [Docker Compose Reference](https://docs.docker.com/compose/compose-file/)
- [Dockerfile Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [Spring Boot Docker Guide](https://spring.io/guides/topicals/spring-boot-docker/)
- [PostgreSQL Docker Hub](https://hub.docker.com/_/postgres)

## 💡 Tips

```bash
# Alias úteis (adicione no seu .bashrc ou .zshrc)
alias dc='docker-compose -f docker-compose.dev.yml'
alias dcup='docker-compose -f docker-compose.dev.yml up -d'
alias dcdown='docker-compose -f docker-compose.dev.yml down'
alias dclogs='docker-compose -f docker-compose.dev.yml logs -f'
alias dcrestart='docker-compose -f docker-compose.dev.yml restart'

# Uso:
dc ps
dcup
dclogs app
dcrestart app
```
