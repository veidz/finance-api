# Instruções para o GitHub Copilot — Projeto: Finance API (Java Spring Boot)

## Objetivo

- Criação de uma API de finanças em Java Spring Boot seguindo nossas regras: Clean Code, Clean Architecture, TDD e KISS.
- Padronizar ambiente de desenvolvimento em VS Code (extensões, atalhos, tasks, devcontainer).
- Fazer o mínimo necessário para cada tarefa (TDD -> falha -> implementar -> refatorar) e depois evoluir.

## Princípios de desenvolvimento (sempre)

- Clean Code: nomes claros, métodos curtos (< 25 linhas quando possível), evitar comentários que expliquem o óbvio.
- Clean Architecture / Hexagonal: separar camadas — controller (entrada), use-cases/services (regras de negócio), repositories/gateways (persistência), domain (entidades/VOs).
- TDD: escrever testes unitários primeiro, fazer os testes falharem, implementar código mínimo para passar, refatorar mantendo testes.
- KISS: implementar a solução mais simples que satisfaça o requisito.
- YAGNI: não implementar funcionalidades só porque "poderia ser útil".
- Pequenos commits e PRs: cada PR deve entregar uma funcionalidade testada e revisável.
- Single Responsibility: classes/métodos com responsabilidade única.

## Regras para o Copilot ao gerar código

1. Sempre gerar testes primeiro:
   - unit test cobrindo casos happy path e edge cases mínimos.
   - usar Mockito para mocks de dependências.
   - dar nomes descritivos: shouldWhenThen e shouldReturnWhenGiven.
2. Gerar somente o código necessário para passar os testes. Evitar "boilerplate" extra.
3. Usar DTOs para entrada/saída nos controllers. Mapear com MapStruct ou mapeio manual simples se for menos dependência.
4. Controllers devem delegar tudo a um service (use-case). Nada de lógica de negócio no controller.
5. Services devem depender de interfaces (ports). Implementações concretas ficam em adapters/repository.
6. Validar entradas com javax.validation (@Valid, @NotNull etc) e retornar erros 400 com DTOs de erro padronizados.
7. Tratar exceções em um @ControllerAdvice centralizado, traduzindo para códigos HTTP e mensagens amigáveis.
8. Evitar primitives scattering: usar Value Objects para conceitos como Money (valor + currency).
9. Logar em nível apropriado (debug/info/warn/error). Não logar segredos.
10. Usar maven wrapper (./mvnw). Gerar pom com plugins: surefire, failsafe, jacoco, spotbugs, checkstyle.

## Configuração do VS Code (essencial)

- Extensões recomendadas:
  - Language Support for Java(TM) by Red Hat
  - Debugger for Java
  - Maven for Java
  - Spring Boot Extension Pack (Pivotal)
  - Lombok Annotations Support (se usar Lombok)
  - Test Runner for Java
  - SonarLint (opcional)
  - vscode-pull-request-github (opcional)
  - GitLens
  - Docker
- Settings (sugestões no workspace settings.json):
  - "java.configuration.runtimes": [{ "name":"JavaSE-17", "path":"/usr/lib/jvm/java-17-openjdk" }]
  - "java.format.settings.url": "./.vscode/eclipse-formatter.xml" (se usar)
  - "editor.formatOnSave": true (opcional)
- Atalhos úteis:
  - Run current test: Ctrl+Shift+T (ou via Test Explorer)
  - Debug test: clicar em "Debug Test" no Test Explorer
  - Run Spring Boot app: use a paleta de comandos (F5 com launch config)
- Devcontainer: usar o .devcontainer já provido para padronizar ambiente.

## Tasks e Launch (exemplos)

- tasks.json:
  - "mvn: package" -> ./mvnw -DskipTests package
  - "mvn: test" -> ./mvnw test
- launch.json:
  - Profile para debug Spring Boot (main class org.springframework.boot.loader.JarLauncher ou classe Application)

## CI e qualidade (instruções durante geração)

- Gerar pom.xml com profiles:
  - default: build + unit tests
  - integration-tests: runs integration tests (failsafe) contra Testcontainers
- Incluir plugins: jacoco (coverage), maven-checkstyle-plugin, spotbugs-maven-plugin.
- Garantir que o código gerado passe no CI: evitar dependências experimentais sem justificar.

## Docker e deploy

- Gerar Dockerfile multi-stage. Copilot deve ajustar se adicionar dependências nativas.
- Gerar docker-compose para dev com Postgres e app.
- Não codar credenciais diretamente: usar variáveis de ambiente.
