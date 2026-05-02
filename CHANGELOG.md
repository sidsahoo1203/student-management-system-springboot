# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [0.1.0] - 2026-05-01

### Added

#### Architecture & Modernization

- **Multi-module Maven structure**: Refactored from monolith to modular layout (parent, web modules)
- **Java 25 runtime**: Upgraded from Java 21 to Java 25 (LTS equivalent)
- **Spring Boot 3.5.0**: Upgraded from 3.4.0 for latest security and features
- **Maven Wrapper 4.0.0-rc-5**: Updated for consistent build environment

#### Security & Compliance

- **CVE remediation**: Fixed HIGH-severity CVE-2025-49146 in PostgreSQL JDBC (42.7.5 → 42.7.8)
- **OAuth2/OIDC authentication**: Added `spring-boot-starter-oauth2-client` with `SecurityConfig` for external auth providers (Keycloak/Auth0)
- **Externalized configuration**: Support for Spring profiles (local, production) with environment-based overrides
- **Vault integration guide**: Added `vault-README.md` for HashiCorp Vault setup (not committed to repo)

#### Observability & Monitoring

- **Spring Boot Actuator**: Exposed health, info, prometheus, env, metrics endpoints
- **Prometheus metrics**: Integrated `micrometer-registry-prometheus` for metrics scraping
- **Structured JSON logging**: Added `logstash-logback-encoder` with conditional JSON logging via `LOG_JSON` env var
- **Actuator configuration**: Dynamic endpoint exposure in `application.yml`

#### Testing & Quality Assurance

- **TestContainers integration tests**: Added PostgreSQL container-based testing (`StudentRepositoryIntegrationTest`)
- **JUnit 5 + AssertJ**: Modern test assertions and annotations
- **OWASP Dependency-Check SCA**: Automated CVE scanning in CI (fails on HIGH/CRITICAL)
- **CycloneDX SBOM**: Bill of materials generation for compliance
- **Gatling performance tests**: Load testing simulation with configurable ramp-up and sustained load profiles (`StudentManagementSimulation`)

#### CI/CD & Deployment

- **GitHub Actions workflow**: Automated build, test, SCA, SBOM generation (`.github/workflows/ci.yml`)
- **Docker containerization**:
  - Multi-stage Dockerfile using JRE 25
  - Docker Compose setup with PostgreSQL 15 for local development
  - `.dockerignore` to keep build context lean
- **Artifact uploads**: Test reports, dependency-check results, SBOM in CI

#### Configuration Management

- **Externalized secrets**: Environment variables for datasource credentials, OAuth2 client secrets
- **Profile-based configs**: `application.yml`, `application-local.yml`, `application-production.yml`
- **No secrets in repo**: Git-safe configuration strategy

#### Documentation

- **Comprehensive onboarding README**: Quick start, architecture, build/run commands, troubleshooting
- **Architecture overview**: `.github/architecture.md` with component responsibilities
- **Enterprise roadmap**: `.github/enterprise-roadmap.md` with 11-step modernization plan
- **Feature-specific guides**: OAuth2, Docker, Vault, Testing/SCA, Performance testing docs
- **Developer workflow**: Contributing guidelines, local setup, test execution

### Changed

- Migrated `pom.xml` from single-module to parent POM with `web` module
- Restructured logging configuration to support JSON and plain-text modes
- Updated `StudentmanagementApplicationTests` to explicitly load application class (Java 25 compatibility)
- Enhanced security configuration with OAuth2 login and logout handling

### Dependencies

**Added:**

- `org.springframework.boot:spring-boot-starter-oauth2-client:3.5.0`
- `org.springframework.boot:spring-boot-starter-actuator:3.5.0`
- `io.micrometer:micrometer-registry-prometheus` (BOM version)
- `net.logstash.logback:logstash-logback-encoder:7.4`
- `org.testcontainers:testcontainers:1.19.8`
- `org.testcontainers:postgresql:1.19.8`
- `org.testcontainers:junit-jupiter:1.19.8`
- `io.gatling.highcharts:gatling-charts-highcharts:3.10.5`

**Updated:**

- Parent: `org.springframework.boot:spring-boot-starter-parent:3.5.0` (from 3.4.0)
- `org.postgresql:postgresql:42.7.8` (from 42.7.5)
- `maven-compiler-plugin:3.14.0` (pinned via pluginManagement)
- `maven-surefire-plugin:3.5.3` (pinned via pluginManagement)

**Versions Set:**

- Java: `25` (target + source)
- Maven Wrapper: `apache-maven/4.0.0-rc-5`

### Security Fixes

- **CVE-2025-49146 (PostgreSQL JDBC)**: Patched via dependency upgrade (42.7.5 → 42.7.8)
- **Dependency scanning enforcement**: HIGH/CRITICAL CVE scanning mandatory in CI

### Known Limitations

- OAuth2 role mapping requires custom `OAuth2UserService` implementation (template provided in `README_OAUTH.md`)
- Gatling load tests require running app on port 8080 (configurable in simulation)
- TestContainers requires Docker daemon running locally

---

## Future Roadmap

- [ ] Kubernetes manifest templates (deployment, service, ingress)
- [ ] OpenTelemetry distributed tracing setup
- [ ] E2E tests with Selenium/Playwright
- [ ] Semantic-release automated versioning
- [ ] GitHub package registry artifact publishing
- [ ] Custom metrics and business intelligence dashboards
- [ ] API documentation (OpenAPI/Swagger)
- [ ] Rate limiting and API quotas

---

## Release Process

See [RELEASE.md](RELEASE.md) for version management and release workflow.

---

**Project maintained by**: Development Team  
**Last updated**: May 1, 2026
