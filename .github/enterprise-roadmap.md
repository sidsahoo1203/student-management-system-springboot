# Enterprise Upgrade Roadmap

Priority sequence and acceptance criteria for making the project enterprise-ready.

1. Convert to multi-module Maven

- Goal: Separate concerns into `core`, `persistence`, `service`, `web`, `integration-tests`, `ops`.
- Acceptance: `mvn -T1C clean test-compile` at repo root succeeds; modules build independently.

2. Add CI/CD (GitHub Actions)

- Goal: Add `ci.yml` with build, test, SCA (OWASP Dependency-Check or `appmod-validate-cves-for-java`), and PR checks.
- Acceptance: PR run shows green checks and publishes SBOM artifact.

3. Dockerize + Compose / K8s manifests

- Goal: Add `Dockerfile` and `docker-compose.yml` for local dev, k8s manifests for production.
- Acceptance: App runs via Docker Compose and connects to Postgres container.

4. Externalize configuration & secrets

- Goal: Support profiles and external secret store (HashiCorp Vault or environment variables). Add sample `application.yml` with placeholders.
- Acceptance: Secrets not stored in repo; app loads DB credentials from env variables.

5. Implement OAuth2/OIDC

- Goal: Replace in-memory auth with OAuth2/OIDC (Auth0/Keycloak example), roles mapping.
- Acceptance: Login via external provider, roles enforced on controller endpoints.

6. Observability: logging, metrics, tracing

- Goal: Add Micrometer (Prometheus) and OpenTelemetry tracing, structured JSON logs.
- Acceptance: Metrics endpoint exposed, traces visible in local collector.

7. Testing: unit, integration, e2e

- Goal: Add TestContainers for integration, Playwright/Selenium for e2e.
- Acceptance: Integration tests run in CI; e2e runs locally via script.

8. Dependency scanning & SCA

- Goal: Add Dependency-Check or Snyk/GitHub Dependabot and SBOM generation.
- Acceptance: CI fails on high/critical CVEs (configurable threshold).

9. Performance & load testing

- Goal: Add Gatling scenarios and baseline reports.
- Acceptance: Baseline report created and stored as CI artifact.

10. Documentation & onboarding README

- Goal: Developer README, architecture docs, contribution guidelines.
- Acceptance: New dev can run app locally in <10 minutes following README.

11. Release process & changelog

- Goal: Add semantic-release or Maven release workflow and CHANGELOG.md.
- Acceptance: Releases created via GitHub Actions with artifacts.

Estimate: 2-4 weeks incremental work depending on approvals and infra.

---

Generated on 2026-05-01.
