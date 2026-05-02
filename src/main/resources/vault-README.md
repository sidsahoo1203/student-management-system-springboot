Configuring external secrets (HashiCorp Vault)

1. Use Spring Cloud Vault (optional):
   - Add dependency: `org.springframework.cloud:spring-cloud-starter-vault-config`
   - Configure bootstrap properties (or use `spring.config.import=vault://...` in Spring Boot 3.4+)

2. Example `bootstrap.properties` or `application.yml` snippet:

```yaml
spring:
  cloud:
    vault:
      uri: ${VAULT_URI:http://localhost:8200}
      token: ${VAULT_TOKEN}
      kv:
        enabled: true
        backend: secret
        default-context: application
```

3. Best practices:

- Do not commit `VAULT_TOKEN` to repo; use CI secrets or environment variables in deployment descriptors.
- Use role-based access and minimal privileges for service tokens.
- Rotate Vault tokens regularly and use dynamic secrets where possible.

4. Alternative: Use environment variables or Kubernetes Secrets mounted as files.
