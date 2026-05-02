# Architecture Overview

## High-level components

- **Web (UI & Controllers)**: `controller` package. Handles HTTP requests, form POSTs, renders Thymeleaf templates in `templates/`.
- **API / DTOs**: `dto` package. Data transfer objects used by controllers and service layer.
- **Service Layer**: `service` package. Business logic, orchestration, transaction boundaries.
- **Persistence / Repositories**: `repository` package. Spring Data JPA repositories, entity mappings in `model`.
- **Domain Models**: `model` package. JPA entities for Student, Course, Enrollment, etc.
- **Configuration**: `config` package. Datasource, security, and app configuration.
- **Security**: `config` + `controller` + `service` (Spring Security configuration and auth logic).
- **Exception Handling**: `exception` package for centralizing error handling and custom exceptions.

## Suggested Modularization (multi-module Maven)

- `parent` (packaging `pom`): manages BOM, properties, plugin versions.
- `core` (jar): domain (`model`), shared utils, DTOs.
- `persistence` (jar): JPA repositories, Hibernate configs, migrations.
- `service` (jar): business logic, services, transactional boundaries.
- `web` (war or jar): controllers, Thymeleaf templates, static assets, security config.
- `integration-tests` (jar): TestContainers integration tests and e2e test harness.
- `ops` (resources): Dockerfiles, k8s manifests, CI templates, deployment scripts.

This split keeps code clean, supports independent testing, and enables teams to own modules.

## Responsibilities and boundaries

- Keep entities and repository interfaces in `persistence` to avoid web-layer dependencies on JPA.
- `core` contains pure domain POJOs and DTOs (no Spring annotations if possible).
- Service layer depends on `persistence` and `core`; `web` depends on `service` and `core`.
- `ops` is non-Java and contains deployment/infra manifests.

## Immediate next changes to prepare for modularization

1. Add a parent `pom.xml` at project root with module list.
2. Move packages into module folders (or create new modules referencing current source folder via refactor).
3. Add module-level `pom.xml` files with minimal dependencies.
4. Keep current repository working copy in `web` module temporarily to reduce disruption.

---

Generated from repository inspection on 2026-05-01.
