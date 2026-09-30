# quarkus-lab

Telemetry-focused PoCs and demo apps built with Quarkus. Each subdirectory is an
independent Quarkus application exploring an observability extension.

- **`otel-*`** — OpenTelemetry demos (tracing, metrics, logs, fault tolerance, reactive routes, AWS, Kubernetes, MCP)
- **`micrometer-*`** — Micrometer metrics demos (Prometheus, LGTM, OTel bridge, custom metrics, filters, LangChain4j)
- **`infra/`** — Docker Compose stacks for local backends (Jaeger, OTel Collector, Prometheus, PostgreSQL, Grafana LGTM)

All modules are aligned to **Quarkus 3.40.1** and share the parent POM
`com.brunobat:quarkus-lab` (Java 21).

## Build & run

There is no root Maven wrapper — use system `mvn` from the root, or a module's own `./mvnw`.

```shell
mvn quarkus:dev -pl <module>     # run a module in dev mode
mvn package -pl <module>         # build one module
mvn package                      # build all
```

All modules build and `mvn test` passes. Some tests are `@Disabled` because they need
external infrastructure (e.g. `otel-mcp-grafana` requires a running Grafana Tempo MCP server).
Start the relevant `infra/` stack before running those.
