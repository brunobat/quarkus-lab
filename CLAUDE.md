# Quarkus Lab

Telemetry-focused PoC and demo monorepo using Quarkus. Each subdirectory is a standalone demo exploring an observability extension; they were built organically and are intentionally independent (no cross-module dependencies).

## Project Structure

Multi-module Maven project (`pom.xml` at root with `<packaging>pom</packaging>`). Each subdirectory is an independent Quarkus application with its own `pom.xml` and dependencies, sharing the parent POM `com.brunobat:quarkus-lab:1.0-SNAPSHOT`.

- **`otel-*`** — OpenTelemetry demos (tracing, metrics, logs, fault tolerance, reactive routes, AWS, Kubernetes, MCP)
- **`micrometer-*`** — Micrometer metrics demos (Prometheus, LGTM, OTel bridge, custom metrics, filters, LangChain4j)
- **`infra/`** — Docker Compose stacks (Jaeger, OTel Collector, Prometheus, PostgreSQL, Grafana LGTM)

## Shared parent POM

The root `pom.xml` centralizes config so modules stay lean and aligned. Defined once in the parent and inherited by all modules:

- `quarkus.platform.version` — **3.40.1** (all modules are aligned; do not re-scatter per-module versions)
- `quarkus.platform.artifact-id` = `quarkus-bom`, plus a default `quarkus.platform.group-id` = `io.quarkus`
- The `quarkus-bom` import in `<dependencyManagement>`
- `compiler-plugin.version`, `maven.compiler.release` (21), `surefire-plugin.version`, source/reporting encodings, and `skipITs` (true)

Kept **per-module** on purpose (preserve these):

- `quarkus.platform.group-id` — overrides the parent default; most modules use `io.quarkus`, a few use `io.quarkus.platform`.
- A commented-out `<!-- <quarkus.platform.version>999-SNAPSHOT</...> -->` line — uncomment locally to test a module against the Quarkus main build. These are handy for manual testing; leave them in place.
- Any extra BOM a module needs (e.g. `quarkus-langchain4j-bom`, `quarkus-amazon-services-bom`).
- The `native` profile's `<skipITs>false</skipITs>` override (re-enables ITs for native builds).

## Build & Run

- Java 21 required.
- There is **no root Maven wrapper**; each module has its own `./mvnw`. Use system `mvn` from the root with `-pl`, or a module's own wrapper.
- Run a module in dev mode: `mvn quarkus:dev -pl <module-name>`
- Build a single module: `mvn package -pl <module-name>`
- Build all: `mvn package` (from root)
- Native build: `mvn package -Dnative -pl <module-name>`

## Conventions

- Config format: `application.properties` (never YAML).
- Packages: `org.acme.*` (most modules) or `com.brunobat.*` (newer modules) — match the module you're in.
- Testing: `@QuarkusTest` + REST Assured for unit tests; `@QuarkusIntegrationTest` extending the unit test class for integration tests (skipped by default via `skipITs`).
- The JUnit test extension is `io.quarkus:quarkus-junit` — **not** `quarkus-junit5`, which has been a relocation to `quarkus-junit` since Quarkus 3.31.
- Common endpoints: `/hello`, `/greeting`.
- All modules build and `mvn test` passes. Some tests are `@Disabled` because they need external infra (e.g. `otel-mcp-grafana` requires a live Grafana Tempo MCP server on `localhost:3200`). A few modules have no HTTP endpoint (CLI, MCP-protocol server, reactive messaging) and are tested differently or not at all.
- Infrastructure for local dev is in `infra/` subdirectories via `docker-compose.yml`.
- `target/` build output is git-ignored globally.
