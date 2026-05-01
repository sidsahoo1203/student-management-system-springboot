Upgrade Java runtime to Java 25 and related build updates

Summary
- Bumped `java.version` to 25
- Upgraded Spring Boot parent to 3.5.0
- Updated Maven wrapper distribution to Apache Maven 4.0.0-rc-5
- Pinned `maven-compiler-plugin` and `maven-surefire-plugin` versions for reproducible builds
- Adjusted test `StudentmanagementApplicationTests` to explicitly reference the application class for reliable context loading

Validation performed
- Installed JDK 25 locally and validated `./mvnw.cmd clean test` passes (1/1 tests)
- Ran dependency effective-POM analysis and CVE scan; one high CVE found for `org.postgresql:postgresql:42.7.5` (see notes)

Notes & recommended follow-up
- Address PostgreSQL JDBC CVE: upgrade driver when a patched release is available or apply recommended connection configuration workarounds (`sslMode=verify-full`).
- Consider promoting Maven wrapper from RC to GA when available.
- Run a dependency CVE scan pipeline (CycloneDX/OWASP Dependency-Check) in CI for continuous monitoring.

Files changed
- `pom.xml` (java.version, parent)
- `.mvn/wrapper/maven-wrapper.properties`
- `src/test/.../StudentmanagementApplicationTests.java`

Please review and merge to `main` when ready. If you prefer, I can open the PR and push these changes for you (requires remote access).
