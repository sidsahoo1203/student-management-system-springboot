# Student Management System - Enterprise Edition

A Spring Boot 3.5.0 application for student lifecycle management, with enterprise-grade features including OAuth2 authentication, observability, containerization, and comprehensive testing.

**Java 25** | **Spring Boot 3.5.0** | **PostgreSQL 15** | **Multi-module Maven**

---

## Quick Start (< 10 minutes)

### Prerequisites

- **Java 25** (JDK)
- **Maven 4.0.0+** (via wrapper)
- **Docker & Docker Compose** (for local database + containerized deployment)

### 1. Clone and build

```bash
git clone <repo-url>
cd student-management-system-springboot-main/studentmanagement
mvn clean package
```

### 2. Local development (H2 in-memory database)

```bash
java -jar web/target/studentmanagement-web-0.0.1-SNAPSHOT.jar
```

Open http://localhost:8080 in your browser.

### 3. Docker Compose (PostgreSQL)

```bash
mvn -DskipTests package
docker-compose up --build
```

Access http://localhost:8080 after containers start.

---

## Project Structure

```
studentmanagement/
├── pom.xml                          # Parent POM (multi-module)
├── web/                             # Web module (controllers, views, config)
│   ├── pom.xml
│   └── src/main/java/...
├── src/
│   ├── main/
│   │   ├── java/com/cwm/studentmanagement/
│   │   │   ├── config/              # Spring config, security
│   │   │   ├── controller/          # HTTP controllers
│   │   │   ├── dto/                 # Data transfer objects
│   │   │   ├── exception/           # Error handling
│   │   │   ├── model/               # JPA entities
│   │   │   ├── repository/          # Spring Data repositories
│   │   │   ├── service/             # Business logic
│   │   │   └── StudentmanagementApplication.java
│   │   └── resources/
│   │       ├── application.yml      # Base config
│   │       ├── application-local.yml        # Local profile (H2)
│   │       ├── application-production.yml   # Production profile
│   │       ├── logback-spring.xml   # Logging config
│   │       └── templates/           # Thymeleaf templates
│   └── test/
│       └── java/...                 # Unit & integration tests
├── .github/
│   ├── workflows/ci.yml             # GitHub Actions CI/CD pipeline
│   ├── architecture.md              # Architecture overview
│   └── enterprise-roadmap.md        # Enterprise feature roadmap
├── Dockerfile                       # Container image
├── docker-compose.yml               # Local dev environment
└── README*.md                       # Feature-specific docs
```

---

## Building & Running

### Maven commands

```bash
# Build with tests
mvn clean package

# Build without tests (faster)
mvn clean package -DskipTests

# Run only unit/integration tests
mvn test

# Run Gatling performance tests (app must be running on :8080)
mvn gatling:test -Dgatling.simulationClass=com.cwm.studentmanagement.perf.StudentManagementSimulation

# Run dependency check (SCA scanning)
mvn org.owasp:dependency-check-maven:check -DfailOnCVSS=7 -Dformat=ALL
```

### Application profiles

Launch with profile via `SPRING_PROFILES_ACTIVE` env var:

```bash
# Local (H2 database, debug logging)
SPRING_PROFILES_ACTIVE=local java -jar web/target/*.jar

# Production (PostgreSQL, env-var based config)
SPRING_PROFILES_ACTIVE=production \
  SPRING_DATASOURCE_URL=jdbc:postgresql://db.example.com:5432/studentdb \
  SPRING_DATASOURCE_USERNAME=user \
  SPRING_DATASOURCE_PASSWORD=secret \
  java -jar web/target/*.jar
```

---

## Key Features

### 🔐 Security

- **Spring Security** with role-based access control (RBAC)
- **OAuth2/OIDC** integration ready (Keycloak/Auth0 compatible)
- See [README_OAUTH.md](README_OAUTH.md) for setup guide

### 🌐 API & Web

- **Spring MVC** controllers for web endpoints
- **Thymeleaf** templating for HTML views
- **JPA/Hibernate** ORM with automatic schema management
- Multi-database support: H2 (dev), PostgreSQL (prod), MySQL (optional)

### 📊 Observability

- **Spring Actuator** with health, metrics, and environment endpoints
- **Prometheus** metrics via Micrometer
- **Structured JSON logging** (optional via `LOG_JSON=true` env var)
- See [README_DOCKER.md](README_DOCKER.md) for metrics scraping

### 🧪 Testing

- **Unit tests** with Spring Boot Test
- **Integration tests** with TestContainers (PostgreSQL)
- **Gatling** load/performance testing
- See [README_TESTING_SCA.md](README_TESTING_SCA.md) for details

### 🔒 Software Composition Analysis

- **OWASP Dependency-Check** in CI pipeline (fails on HIGH/CRITICAL CVEs)
- **CycloneDX SBOM** generation
- Artifacts uploaded to GitHub Actions workflow runs

### 🐳 Containerization

- **Docker** image with JRE 25
- **Docker Compose** for local PostgreSQL + app stack
- Kubernetes-ready manifests (add manually)

### 🚀 Configuration Management

- **Externalized config** via `application-*.yml` and environment variables
- **HashiCorp Vault** integration guide in [vault-README.md](src/main/resources/vault-README.md)
- Secrets NOT committed to repository

---

## Configuration Guide

See [vault-README.md](src/main/resources/vault-README.md) and [README_OAUTH.md](README_OAUTH.md) for secrets and auth setup.

### Essential env vars (production)

```bash
SPRING_PROFILES_ACTIVE=production
SPRING_DATASOURCE_URL=jdbc:postgresql://...
SPRING_DATASOURCE_USERNAME=...
SPRING_DATASOURCE_PASSWORD=...
OAUTH_CLIENT_ID=...           # For OAuth2
OAUTH_CLIENT_SECRET=...
OAUTH_ISSUER_URI=...
LOG_JSON=true                 # For JSON logging
PORT=8080                     # Server port
```

---

## Detailed Documentation

| Topic                 | File                                                           |
| --------------------- | -------------------------------------------------------------- |
| Architecture & Design | [.github/architecture.md](.github/architecture.md)             |
| Enterprise Roadmap    | [.github/enterprise-roadmap.md](.github/enterprise-roadmap.md) |
| OAuth2/OIDC Setup     | [README_OAUTH.md](README_OAUTH.md)                             |
| Vault & Secrets       | [vault-README.md](src/main/resources/vault-README.md)          |
| Docker Deployment     | [README_DOCKER.md](README_DOCKER.md)                           |
| Testing & SCA         | [README_TESTING_SCA.md](README_TESTING_SCA.md)                 |
| Performance Testing   | [README_PERF_TESTING.md](README_PERF_TESTING.md)               |

---

## CI/CD Pipeline

GitHub Actions workflow (`.github/workflows/ci.yml`):

1. **Build**: Compile and test-compile
2. **Tests**: Run unit + integration tests
3. **SCA**: OWASP Dependency-Check (fails if CVEs ≥ HIGH)
4. **SBOM**: Generate CycloneDX bill of materials
5. **Artifacts**: Upload test results, reports, SBOM

On **main/master** push or PR.

---

## Development Workflow

### 1. Local setup

```bash
git clone <repo>
cd studentmanagement
mvn clean install
```

### 2. Run tests before committing

```bash
mvn test
```

### 3. Build locally

```bash
mvn clean package
```

### 4. Docker development

```bash
docker-compose up --build
# App on http://localhost:8080
# PostgreSQL on localhost:5432
```

### 5. Run performance tests

```bash
# In one terminal, start the app
java -jar web/target/*.jar

# In another terminal, run Gatling
mvn gatling:test -Dgatling.simulationClass=com.cwm.studentmanagement.perf.StudentManagementSimulation
```

### 6. Check for dependency updates

```bash
mvn versions:display-dependency-updates
```

---

## Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Make changes and test locally: `mvn test`
3. Commit with clear messages: `git commit -m "feat: add new feature"`
4. Push and create a Pull Request
5. GitHub Actions CI will validate the build
6. Merge once all checks pass

---

## Troubleshooting

### Build fails on TestContainers

**Error**: `Cannot connect to Docker daemon`

**Solution**: Ensure Docker is running.

```bash
docker ps  # Verify Docker is active
```

### Port 8080 already in use

**Solution**: Change the port via environment variable:

```bash
PORT=8081 java -jar web/target/*.jar
```

### Database connection errors in production

**Solution**: Verify env vars are set:

```bash
echo $SPRING_DATASOURCE_URL
echo $SPRING_DATASOURCE_USERNAME
```

### OAuth2 login not working

**Solution**: Check `application-production.yml` configuration and provider setup. See [README_OAUTH.md](README_OAUTH.md).

---

## Support & Resources

- **Spring Boot docs**: https://spring.io/projects/spring-boot
- **Spring Security**: https://spring.io/projects/spring-security
- **Spring Data JPA**: https://spring.io/projects/spring-data-jpa
- **Docker docs**: https://docs.docker.com
- **Gatling docs**: https://gatling.io/docs

---

## License

See [LICENSE](LICENSE) file.

---

**Last updated**: May 1, 2026  
**Java Version**: 25  
**Spring Boot Version**: 3.5.0
