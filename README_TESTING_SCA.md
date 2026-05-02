Testing & Security Scanning

## Integration Tests (TestContainers)

The project includes integration tests using **TestContainers** that spin up real PostgreSQL containers for testing.

### Running integration tests locally:

```bash
mvn test
```

### Test Classes

- `StudentRepositoryIntegrationTest.java`: Demonstrates TestContainers with PostgreSQL. Tests:
  - Save and retrieve students
  - Query by active status
  - Email existence checks

### Test Dependencies

- `org.testcontainers:testcontainers:1.19.8`
- `org.testcontainers:postgresql:1.19.8`
- `org.testcontainers:junit-jupiter:1.19.8`

Docker must be running locally for TestContainers tests to work.

---

## Security Scanning (SCA)

The CI pipeline enforces **Software Composition Analysis (SCA)** using OWASP Dependency-Check.

### Configuration

- **Tool**: OWASP Dependency-Check Maven Plugin
- **Fail threshold**: CVE severity ≥ 7 (HIGH/CRITICAL)
- **Artifacts generated**: dependency-check reports (HTML, JSON, XML)

### Local execution

```bash
# Run dependency-check locally (fails on HIGH/CRITICAL CVEs)
mvn org.owasp:dependency-check-maven:check -DfailOnCVSS=7 -Dformat=ALL

# View report
open target/dependency-check-report.html
```

### CI/CD Integration

The `.github/workflows/ci.yml` workflow:

1. Runs build and tests
2. Executes OWASP Dependency-Check (fails if CVEs found with severity ≥ 7)
3. Generates CycloneDX SBOM
4. Uploads artifacts:
   - Test results (junit-results/)
   - Dependency-check report
   - SBOM (bom.json)

### Adjusting the threshold

To change the fail threshold (lower = stricter):

```bash
# Fail on MEDIUM and above (CVSS ≥ 5.5)
mvn org.owasp:dependency-check-maven:check -DfailOnCVSS=5.5

# Fail on ANY CVE
mvn org.owasp:dependency-check-maven:check -DfailOnCVSS=0
```

Update in `.github/workflows/ci.yml` if changing the default.

---

## Best Practices

- **Run tests before PR**: `mvn clean test` ensures integration tests pass
- **Check CVEs regularly**: Even if build passes, review the generated reports for "low" severity CVEs
- **Keep dependencies updated**: Regularly run `mvn dependency:display-updates` to find newer versions
- **Review SBOM**: The generated bom.json tracks all dependencies and versions for compliance

---

Generated: May 1, 2026
