# otel-mcp-client

A Quarkus CLI application that connects to an MCP server using [quarkus-langchain4j-mcp](https://docs.quarkiverse.io/quarkus-langchain4j/dev/mcp.html) and invokes tools and prompts via the [Streamable HTTP transport](https://modelcontextprotocol.io/specification/2025-03-26/basic/transports#streamable-http).

Telemetry is exported via OpenTelemetry (Micrometer OTel bridge).

## Prerequisites

- Java 21
- The `otel-mcp-server` module running on port 8080

## Start the MCP server

```bash
cd ../otel-mcp-server && mvn quarkus:dev
```

## Build

```bash
mvn package
```

## Available commands

### List tools

```bash
java -jar target/quarkus-app/quarkus-run.jar list-tools
```

### Call the `toLowerCase` tool

```bash
java -jar target/quarkus-app/quarkus-run.jar toLowerCase HELLO
```

### Call the `answer` tool

```bash
java -jar target/quarkus-app/quarkus-run.jar answer 'What is Quarkus?'
```

### Call the `code-assist` prompt

```bash
java -jar target/quarkus-app/quarkus-run.jar code-assist java
```

## Configuration

The MCP server URL is configured in `src/main/resources/application.properties`:

```properties
quarkus.langchain4j.mcp.server.transport-type=streamable-http
quarkus.langchain4j.mcp.server.url=http://localhost:8080/mcp
```

## Running the tests

Tests start an embedded MCP server in the same JVM, so no external server is needed:

```bash
mvn test
```
