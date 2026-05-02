# Release Process

This document describes the release workflow for Student Management System.

---

## Semantic Versioning

The project follows **Semantic Versioning 2.0.0** ([semver.org](https://semver.org/spec/v2.0.0.html)):

```
MAJOR.MINOR.PATCH[-PRERELEASE]
```

- **MAJOR**: Breaking API or architecture changes (e.g., Java version bump, Spring Boot major upgrade)
- **MINOR**: Backward-compatible feature additions (e.g., new OAuth provider support, new REST endpoint)
- **PATCH**: Backward-compatible bug fixes (e.g., CVE patches, performance improvements)
- **PRERELEASE**: Optional suffix for alpha/beta (e.g., `1.0.0-beta.1`)

---

## Pre-Release Checklist

Before creating a release, ensure:

### 1. Code Quality

```bash
# Run all tests
mvn clean test

# Run SCA scanning (must pass)
mvn org.owasp:dependency-check-maven:check -DfailOnCVSS=7

# Run performance baseline (optional)
mvn gatling:test -Dgatling.simulationClass=com.cwm.studentmanagement.perf.StudentManagementSimulation
```

### 2. Build Verification

```bash
# Build the application
mvn clean package

# Verify Docker image builds
docker build -t studentmanagement:latest .

# Test Docker Compose setup
docker-compose up --build
# ... verify app is accessible at http://localhost:8080
docker-compose down
```

### 3. Dependency Updates

```bash
# Check for outdated dependencies
mvn versions:display-dependency-updates

# Update to patch/minor versions (if desired)
# mvn versions:use-next-releases
# mvn versions:commit  # or revert with mvn versions:revert
```

### 4. Documentation Review

- [ ] `README.md` is up-to-date with latest features
- [ ] `CHANGELOG.md` has entry for new version
- [ ] Feature-specific `README_*.md` files are current
- [ ] `.github/enterprise-roadmap.md` reflects completed items

### 5. Git Status

```bash
# Ensure working directory is clean
git status

# Ensure on main/master branch
git branch

# Latest commits should pass CI
git log --oneline -5
```

---

## Release Steps (Manual Process)

### 1. Create Release Branch

```bash
git checkout -b release/v0.2.0
```

### 2. Update Version in pom.xml

Change version in parent `pom.xml` and `web/pom.xml`:

```xml
<!-- pom.xml -->
<version>0.2.0</version>
```

### 3. Update CHANGELOG.md

Add section for the new version:

```markdown
## [0.2.0] - 2026-05-15

### Added

- Feature X

### Changed

- Behavior Y

### Fixed

- Bug Z
```

### 4. Commit Release Changes

```bash
git add pom.xml CHANGELOG.md
git commit -m "chore: bump version to 0.2.0"
```

### 5. Create Git Tag

```bash
git tag -a v0.2.0 -m "Release version 0.2.0"
```

### 6. Build Release Artifacts

```bash
mvn clean package -DskipTests
# Produces: web/target/studentmanagement-web-0.2.0.jar
```

### 7. Push to Repository

```bash
git push origin release/v0.2.0
git push origin v0.2.0
```

### 8. Create GitHub Release

1. Go to: https://github.com/<owner>/student-management-system-springboot/releases/new
2. Select tag: `v0.2.0`
3. Title: `Release v0.2.0`
4. Description: Copy relevant section from `CHANGELOG.md`
5. Upload artifacts: JAR file and SBOM
6. Mark as "Latest release"
7. Publish

### 9. Merge Back to Main

```bash
git checkout main
git pull origin main
git merge --no-ff release/v0.2.0
git push origin main
```

### 10. Clean Up Release Branch

```bash
git branch -d release/v0.2.0
git push origin :release/v0.2.0
```

---

## Automated Release (Future: GitHub Actions)

Once a CI/CD release workflow is set up (recommended):

```yaml
name: Release

on:
  push:
    tags:
      - "v*"

jobs:
  release:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Build and publish artifacts
        run: mvn clean deploy
      - name: Create GitHub Release
        uses: softprops/action-gh-release@v1
        with:
          files: web/target/*.jar
```

---

## Hotfix Process

For critical security/bug fixes on released versions:

### 1. Create Hotfix Branch

```bash
git checkout -b hotfix/v0.2.1
```

### 2. Apply Fix

```bash
# Make necessary changes
git add <files>
git commit -m "fix: address critical issue"
```

### 3. Update Version and CHANGELOG

```xml
<!-- pom.xml -->
<version>0.2.1</version>
```

Add to `CHANGELOG.md`:

```markdown
## [0.2.1] - 2026-05-16

### Security

- Fixed CVE-XXXX in dependency Y
```

### 4. Tag and Release

```bash
git tag -a v0.2.1 -m "Hotfix v0.2.1"
git push origin hotfix/v0.2.1 v0.2.1
```

### 5. Merge Back

```bash
git checkout main
git merge hotfix/v0.2.1
git push origin main
```

---

## Version Compatibility Matrix

| Java | Spring Boot | PostgreSQL | Status         |
| ---- | ----------- | ---------- | -------------- |
| 25   | 3.5.0       | 15+        | ✅ Current     |
| 21   | 3.4.0       | 13+        | ⚠️ EOL (0.0.1) |
| 17   | 2.7.x       | 12+        | ❌ Unsupported |

---

## Artifact Distribution

Released artifacts are stored:

- **GitHub Releases**: JAR + SBOM (CycloneDX)
- **Docker Hub** (optional): `studentmanagement:0.2.0`
- **Maven Central** (optional): Via GitHub Packages

---

## Post-Release

1. **Announce release**: Update team/stakeholders
2. **Monitor**: Watch for early-adopter issues
3. **Next development cycle**: Bump to SNAPSHOT version
   ```xml
   <version>0.3.0-SNAPSHOT</version>
   ```
4. **Plan next features**: Update roadmap in `.github/enterprise-roadmap.md`

---

## Support

- **Bug reports**: GitHub Issues
- **Security issues**: Email security@example.com (do NOT open public issue)
- **Feature requests**: GitHub Discussions

---

**Last updated**: May 1, 2026
