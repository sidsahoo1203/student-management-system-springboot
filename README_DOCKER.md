Docker local run

1. Build the application JAR locally:

```bash
./mvnw -DskipTests package
```

2. Build and start containers:

```bash
docker-compose up --build
```

3. App will be available at: http://localhost:8080

Notes:

- The `Dockerfile` expects the Spring Boot executable JAR at `target/*-SNAPSHOT.jar`.
- For CI-based builds you can add a multi-stage Docker build using a JDK 25 Maven image when available.
