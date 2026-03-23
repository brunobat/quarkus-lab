# otel-mcp-grafana

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Before you start

This project requires a running grafana-lgtm container supporting an MCP server. At the moment this was created, it didn't exist yet.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```



## Related Guides

- Micrometer OpenTelemetry Bridge ([guide](https://quarkus.io/guides/telemetry-micrometer-to-opentelemetry)): Micrometer registry implemented by the OpenTelemetry SDK
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- LangChain4j Model Context Protocol client ([guide](https://docs.quarkiverse.io/quarkus-langchain4j/dev/index.html)): Provides the Model Context Protocol client-side implementation for LangChain4j
